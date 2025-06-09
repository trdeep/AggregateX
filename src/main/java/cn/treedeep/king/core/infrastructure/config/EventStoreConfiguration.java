package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.domain.EventStore;
import cn.treedeep.king.core.infrastructure.eventstore.EventStoreRepository;
import cn.treedeep.king.core.infrastructure.eventstore.InMemoryEventStore;
import cn.treedeep.king.core.infrastructure.eventstore.JpaEventStore;
import cn.treedeep.king.core.infrastructure.eventstore.SnapshotRepository;
import cn.treedeep.king.core.infrastructure.eventstore.compression.EventCompressor;
import cn.treedeep.king.core.infrastructure.monitoring.EventStoreMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 事件存储配置类
 * <p>
 * 负责根据配置选择合适的事件存储实现
 * <p>
 * 支持两种实现：<br>
 * 1. 内存存储（memory）：适用于开发和测试环境<br>
 * 2. JPA存储（jpa）：适用于生产环境，将事件持久化到数据库
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EventStoreProperties.class)
public class EventStoreConfiguration {

    /**
     * 内存事件存储 - 默认实现
     * 当没有其他EventStore实现时启用
     */
    @Bean
    @ConditionalOnMissingBean(EventStore.class)
    @ConditionalOnProperty(name = "app.event-store.type", havingValue = "memory", matchIfMissing = true)
    public EventStore memoryEventStore() {
        log.info("Using in-memory event store - suitable for development and testing");
        return new InMemoryEventStore();
    }

    /**
     * JPA事件存储配置
     * 只有在JPA和所需Bean都可用时才启用
     */
    @Configuration
    @ConditionalOnClass(name = "org.springframework.data.jpa.repository.JpaRepository")
    @ConditionalOnProperty(name = "app.event-store.type", havingValue = "jpa")
    public static class JpaEventStoreConfiguration {

        @Bean
        @ConditionalOnMissingBean(EventStore.class)
        public EventStore jpaEventStore(
                EventStoreProperties properties,
                EventStoreRepository eventRepository,
                SnapshotRepository snapshotRepository,
                ObjectMapper objectMapper,
                EventStoreMetrics metrics,
                CacheManager cacheManager,
                EventCompressor eventCompressor) {

            log.info("Using JPA event store - suitable for production");
            return new JpaEventStore(
                    eventRepository,
                    snapshotRepository,
                    objectMapper,
                    properties,
                    eventCompressor,
                    metrics,
                    cacheManager);
        }
    }
}
