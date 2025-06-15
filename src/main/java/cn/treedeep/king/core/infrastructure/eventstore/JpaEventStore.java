package cn.treedeep.king.core.infrastructure.eventstore;

import cn.treedeep.king.core.domain.ConcurrentModificationException;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventStore;
import cn.treedeep.king.core.infrastructure.eventstore.compression.EventCompressor;
import cn.treedeep.king.core.infrastructure.monitoring.EventStoreMetrics;
import cn.treedeep.king.shared.properties.EventStoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Supplier;

/**
 * 基于JPA的事件存储实现
 * <p>
 * 适用于生产环境，将事件持久化到数据库中
 * <p>
 * 特性：
 * 1. 事件压缩 - 通过EventCompressor接口支持事件流压缩
 * 2. 快照支持 - 定期创建聚合根状态快照，提高重建性能
 * 3. 性能监控 - 通过Micrometer提供详细的性能指标
 * 4. 批量处理 - 支持大批量事件的高效存储
 * 5. 错误处理 - 提供详细的错误信息和异常处理
 * 6. 缓存支持 - 使用Spring Cache提供多级缓存
 */
@Slf4j
public class JpaEventStore implements EventStore {

    private final EventStoreRepository eventRepository;
    private final SnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;
    private final EventStoreProperties properties;
    private final EventCompressor eventCompressor;
    private final EventStoreMetrics metrics;
    private final CacheManager cacheManager;

    private static final String EVENT_SAVE_ERROR = "保存事件失败，聚合ID: {}，错误: {}";
    private static final String EVENT_SAVE_START = "开始保存事件，聚合ID: {}，事件数量: {}";
    private static final String EVENT_SAVE_COMPLETE = "事件保存完成，聚合ID: {}，已保存: {}事件";
    private static final String SNAPSHOT_CREATE_ERROR = "创建快照失败，聚合ID: {}，错误: {}";
    private static final String VERSION_MISMATCH = "版本冲突，聚合ID: %s，期望版本: %s，实际版本: %s";

    public JpaEventStore(
            EventStoreRepository eventRepository,
            SnapshotRepository snapshotRepository,
            ObjectMapper objectMapper,
            EventStoreProperties properties,
            EventCompressor eventCompressor,
            EventStoreMetrics metrics,
            CacheManager cacheManager) {
        this.eventRepository = eventRepository;
        this.snapshotRepository = snapshotRepository;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.eventCompressor = eventCompressor;
        this.metrics = metrics;
        this.cacheManager = cacheManager;

        // 验证批处理大小配置
        if (properties.getBatchSize() <= 0) {
            log.warn("⚠️ 批处理大小配置异常: {}，将使用默认值1000", properties.getBatchSize());
        }

        log.info("JPA事件存储已初始化，批处理大小: {}, 快照频率: {}, 缓存已启用",
                Math.max(properties.getBatchSize(), 1000),
                properties.getSnapshot().getFrequency());
    }

    @Override
    @Transactional
    public void saveEvents(String aggregateId, List<DomainEvent> events, int expectedVersion) {
        Timer.Sample timer = Timer.start();
        try {
            log.info(EVENT_SAVE_START, aggregateId, events.size());

            verifyVersion(aggregateId, expectedVersion);
            List<DomainEvent> eventsToSave = compressEventsIfNeeded(events);
            int savedCount = saveEventsBatch(aggregateId, eventsToSave, expectedVersion);
            checkAndCreateSnapshot(aggregateId, eventsToSave, expectedVersion + savedCount);

            // 清除相关缓存
            evictCaches(aggregateId);

            log.info(EVENT_SAVE_COMPLETE, aggregateId, savedCount);
            timer.stop(metrics.getEventSaveTimer());
        } catch (Exception e) {
            log.error(EVENT_SAVE_ERROR, aggregateId, e.getMessage(), e);
            throw e;
        }
    }

    private void verifyVersion(String aggregateId, int expectedVersion) {
        // 使用已缓存的事件列表来验证版本
        List<DomainEvent> existingEvents = getEvents(aggregateId);
        if (!existingEvents.isEmpty() && existingEvents.size() != expectedVersion) {
            String message = String.format(VERSION_MISMATCH, aggregateId, expectedVersion, existingEvents.size());
            throw new ConcurrentModificationException(message, expectedVersion, existingEvents.size());
        }
    }

    private List<DomainEvent> compressEventsIfNeeded(List<DomainEvent> events) {
        if (eventCompressor.shouldCompress(events)) {
            log.debug("开始压缩事件流，原始事件数量: {}", events.size());
            List<DomainEvent> compressed = eventCompressor.compress(events);
            log.debug("事件流压缩完成，压缩后事件数量: {}", compressed.size());
            return compressed;
        }
        return events;
    }

    private int saveEventsBatch(String aggregateId, List<DomainEvent> events, int baseVersion) {
        int savedCount = 0;
        int batchSize = Math.max(properties.getBatchSize(), 1000);

        for (int i = 0; i < events.size(); i += batchSize) {
            int end = Math.min(i + batchSize, events.size());
            List<DomainEvent> batch = events.subList(i, end);

            for (DomainEvent event : batch) {
                final DomainEvent eventToSave = event;
                eventToSave.setAggregateId(aggregateId);
                eventToSave.setAggregateVersion((long) baseVersion + i);

                metrics.getEventSaveTimer().record((Supplier<Void>) () -> {
                    eventRepository.save(eventToSave);
                    return null;
                });
                metrics.getEventsSavedCounter().increment();
                savedCount++;
            }
        }
        return savedCount;
    }

    private void checkAndCreateSnapshot(String aggregateId, List<DomainEvent> events, long version) {
        if (!properties.getSnapshot().isEnabled() || events.isEmpty()) {
            return;
        }

        if (version % properties.getSnapshot().getFrequency() == 0) {
            createSnapshot(aggregateId, events.getLast());
        }
    }

    private void createSnapshot(String aggregateId, DomainEvent lastEvent) {
        metrics.getSnapshotSaveTimer().record(() -> {
            try {
                AggregateSnapshot snapshot = new AggregateSnapshot();
                snapshot.setAggregateId(aggregateId);
                snapshot.setAggregateType(lastEvent.getClass().getName());
                snapshot.setVersion(lastEvent.getAggregateVersion());
                snapshot.setSnapshotData(objectMapper.writeValueAsString(lastEvent));
                snapshot.setCreatedAt(OffsetDateTime.now());

                snapshotRepository.save(snapshot);
                metrics.getSnapshotsSavedCounter().increment();

                log.debug("快照创建成功，聚合ID: {}, 版本: {}", aggregateId, snapshot.getVersion());
                return null;
            } catch (JsonProcessingException e) {
                log.error(SNAPSHOT_CREATE_ERROR, aggregateId, e.getMessage());
                throw new RuntimeException("Failed to serialize event for snapshot", e);
            }
        });
    }

    @Override
    @Cacheable(value = "events", key = "#aggregateId")
    public List<DomainEvent> getEvents(String aggregateId) {
        return metrics.getEventReadTimer().record(() -> {
            List<DomainEvent> events = eventRepository.findByAggregateId(aggregateId);
            metrics.getEventsReadCounter().increment(events.size());
            return events;
        });
    }

    @Override
    public List<DomainEvent> getAllEvents() {
        return metrics.getEventReadTimer().record(() -> {
            List<DomainEvent> events = eventRepository.findAll();
            metrics.getEventsReadCounter().increment(events.size());
            return events;
        });
    }

    @Cacheable(value = "snapshots", key = "#aggregateId + ':' + #aggregateType")
    public AggregateSnapshot getLatestSnapshot(String aggregateId, String aggregateType) {
        return metrics.getSnapshotReadTimer().record(() -> {
            AggregateSnapshot snapshot = snapshotRepository
                .findTopByAggregateIdAndAggregateTypeOrderByVersionDesc(aggregateId, aggregateType);
            if (snapshot != null) {
                metrics.getSnapshotsReadCounter().increment();
            }
            return snapshot;
        });
    }

    private void evictCaches(String aggregateId) {
        // 清除事件缓存
        Cache eventsCache = cacheManager.getCache("events");
        if (eventsCache != null) {
            eventsCache.evict(aggregateId);
        }

        // 清除快照缓存 - 由于不知道具体的聚合类型，清除所有相关快照缓存
        Cache snapshotsCache = cacheManager.getCache("snapshots");
        if (snapshotsCache != null) {
            snapshotsCache.clear(); // 这里可以优化为只清除特定聚合的快照
        }
    }
}
