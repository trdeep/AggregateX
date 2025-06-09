package cn.treedeep.king.core.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * 事件存储监控指标
 * <p>
 * 收集和暴露事件存储相关的监控指标
 */
@Getter
@Component
public class EventStoreMetrics {

    private final Counter eventsSavedCounter;

    private final Counter eventsReadCounter;

    private final Counter snapshotsSavedCounter;

    private final Counter snapshotsReadCounter;

    private final Timer eventSaveTimer;

    private final Timer eventReadTimer;

    private final Timer snapshotSaveTimer;

    private final Timer snapshotReadTimer;

    private final Counter eventsArchivedCounter;
    private final Timer eventArchiveTimer;

    // 新增缓存指标
    private final Counter cacheHitsCounter;
    private final Counter cacheMissesCounter;
    private final Timer cacheGetTimer;

    public EventStoreMetrics(MeterRegistry registry) {
        // 事件计数器
        this.eventsSavedCounter = Counter.builder("eventstore.events.saved")
                .description("已保存的事件总数")
                .register(registry);

        this.eventsReadCounter = Counter.builder("eventstore.events.read")
                .description("已读取的事件总数")
                .register(registry);

        // 快照计数器
        this.snapshotsSavedCounter = Counter.builder("eventstore.snapshots.saved")
                .description("已保存的快照总数")
                .register(registry);

        this.snapshotsReadCounter = Counter.builder("eventstore.snapshots.read")
                .description("已读取的快照总数")
                .register(registry);

        // 操作计时器
        this.eventSaveTimer = Timer.builder("eventstore.events.save.time")
                .description("保存事件所需时间")
                .register(registry);

        this.eventReadTimer = Timer.builder("eventstore.events.read.time")
                .description("读取事件所需时间")
                .register(registry);

        this.snapshotSaveTimer = Timer.builder("eventstore.snapshots.save.time")
                .description("保存快照所需时间")
                .register(registry);

        this.snapshotReadTimer = Timer.builder("eventstore.snapshots.read.time")
                .description("读取快照所需时间")
                .register(registry);

        // 归档指标
        this.eventsArchivedCounter = Counter.builder("eventstore.events.archived")
                .description("已归档的事件总数")
                .register(registry);

        this.eventArchiveTimer = Timer.builder("eventstore.events.archive.time")
                .description("归档事件所需时间")
                .register(registry);

        // 新增缓存指标
        this.cacheHitsCounter = Counter.builder("eventstore.cache.hits")
                .description("缓存命中次数")
                .register(registry);

        this.cacheMissesCounter = Counter.builder("eventstore.cache.misses")
                .description("缓存未命中次数")
                .register(registry);

        this.cacheGetTimer = Timer.builder("eventstore.cache.get.time")
                .description("缓存获取操作耗时")
                .register(registry);
    }
}

// 其它监控指标
//AggregateMetrics.java
//DomainEventMetrics.java
//DomainMetrics.java
//MetricsConfiguration.java
