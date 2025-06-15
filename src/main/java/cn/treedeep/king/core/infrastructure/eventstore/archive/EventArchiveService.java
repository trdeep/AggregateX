package cn.treedeep.king.core.infrastructure.eventstore.archive;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.infrastructure.eventstore.EventStoreRepository;
import cn.treedeep.king.core.infrastructure.monitoring.EventStoreMetrics;
import cn.treedeep.king.shared.utils.DateTimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 事件归档服务
 * <p>
 * 负责定期将旧事件归档到归档表中，以提高主事件表的查询性能
 */
@Slf4j
public class EventArchiveService {

    private final ObjectMapper objectMapper;
    private final EventArchiveRepository archiveRepository;
    private final EventStoreRepository eventRepository;
    private final EventStoreMetrics metrics;
    private static final long ARCHIVE_BATCH_SIZE = 1000;
    private static final long EVENT_RETENTION_DAYS = 30; // 事件保留天数

    public EventArchiveService(
            ObjectMapper objectMapper,
            EventArchiveRepository archiveRepository,
            EventStoreRepository eventRepository,
            EventStoreMetrics metrics) {
        this.objectMapper = objectMapper;
        this.archiveRepository = archiveRepository;
        this.eventRepository = eventRepository;
        this.metrics = metrics;
    }

    /**
     * 将单个事件归档
     */
    @Transactional
    public ArchivedEvent archiveEvent(DomainEvent event) {
        try {
            ArchivedEvent archivedEvent = new ArchivedEvent();
            archivedEvent.setAggregateId(event.getAggregateId());
            archivedEvent.setEventType(event.getClass().getName());
            archivedEvent.setEventData(objectMapper.writeValueAsString(event));
            archivedEvent.setAggregateVersion(event.getAggregateVersion());
            archivedEvent.setOriginalTimestamp(event.getOccurredOn());

            archiveRepository.save(archivedEvent);
            metrics.getEventsArchivedCounter().increment();

            log.debug("事件归档成功, 聚合ID: {}, 版本: {}",
                    event.getAggregateId(), event.getAggregateVersion());

            return archivedEvent;
        } catch (Exception e) {
            log.error("事件归档失败, 聚合ID: {}, 版本: {}, 错误: {}",
                    event.getAggregateId(), event.getAggregateVersion(), e.getMessage(), e);
            throw new RuntimeException("Failed to archive event", e);
        }
    }

    /**
     * 批量归档事件
     */
    @Transactional
    public void archiveEvents(List<DomainEvent> events) {
        metrics.getEventArchiveTimer().record(() -> {
            try {
                log.info("开始批量归档事件, 数量: {}", events.size());

                for (int i = 0; i < events.size(); i += (int) ARCHIVE_BATCH_SIZE) {
                    int end = Math.min(i + (int) ARCHIVE_BATCH_SIZE, events.size());
                    List<DomainEvent> batch = events.subList(i, end);

                    batch.forEach(this::archiveEvent);
                }

                log.info("批量归档完成, 共归档: {}", events.size());
            } catch (Exception e) {
                log.error("批量归档失败, 错误: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to archive events batch", e);
            }
        });
    }

    /**
     * 定期执行归档任务，每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void scheduledArchiving() {
        log.info("开始执行定期归档任务");
        OffsetDateTime cutoffDate = DateTimeUtil.now().minusDays(EVENT_RETENTION_DAYS);

        try {
            // 分页查询老事件并归档
             List<DomainEvent> oldEvents = eventRepository.findByOccurredOnBefore(cutoffDate);
             archiveEvents(oldEvents);

            log.info("定期归档任务完成");
        } catch (Exception e) {
            log.error("定期归档任务失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 从归档中恢复事件
     */
    public List<DomainEvent> recoverArchivedEvents(String aggregateId, Long version) {
        try {
            List<ArchivedEvent> archivedEvents = archiveRepository
                    .findByAggregateIdAndVersionLessThanEqual(aggregateId, version);

            return archivedEvents.stream()
                    .map(this::deserializeEvent)
                    .toList();
        } catch (Exception e) {
            log.error("从归档恢复事件失败, 聚合ID: {}, 版本: {}, 错误: {}",
                    aggregateId, version, e.getMessage(), e);
            throw new RuntimeException("Failed to recover archived events", e);
        }
    }

    private DomainEvent deserializeEvent(ArchivedEvent archivedEvent) {
        try {
            Class<?> eventType = Class.forName(archivedEvent.getEventType());
            return (DomainEvent) objectMapper.readValue(archivedEvent.getEventData(), eventType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize archived event", e);
        }
    }
}
