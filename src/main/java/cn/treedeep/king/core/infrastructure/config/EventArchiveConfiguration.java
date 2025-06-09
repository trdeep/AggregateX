package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.infrastructure.eventstore.EventStoreRepository;
import cn.treedeep.king.core.infrastructure.eventstore.archive.EventArchiveRepository;
import cn.treedeep.king.core.infrastructure.eventstore.archive.EventArchiveService;
import cn.treedeep.king.core.infrastructure.monitoring.EventStoreMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 事件归档配置类
 * <p>
 * 只有在JPA可用且启用归档功能时才加载
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.springframework.data.jpa.repository.JpaRepository")
@ConditionalOnProperty(name = "app.event-archive.enabled", havingValue = "true", matchIfMissing = false)
public class EventArchiveConfiguration {

    /**
     * 事件归档服务
     * 只有在JPA和所需Repository都可用时才启用
     */
    @Bean
    public EventArchiveService eventArchiveService(
            ObjectMapper objectMapper,
            EventArchiveRepository archiveRepository,
            EventStoreRepository eventStoreRepository,
            EventStoreMetrics metrics) {
        log.info("Enabling event archive service with JPA repository");
        return new EventArchiveService(objectMapper, archiveRepository, eventStoreRepository, metrics);
    }
}
