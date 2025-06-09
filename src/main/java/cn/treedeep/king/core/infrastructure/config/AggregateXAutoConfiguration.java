package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.domain.DomainEventPublisher;
import cn.treedeep.king.core.infrastructure.eventstore.compression.DefaultEventCompressor;
import cn.treedeep.king.core.infrastructure.eventstore.compression.EventCompressor;
import cn.treedeep.king.shared.properties.KingDataProperties;
import cn.treedeep.king.shared.properties.KingOssProperties;
import cn.treedeep.king.shared.properties.KingProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * AggregateX框架自动配置类
 * <p>
 * 自动配置所有必需的Bean，简化框架集成
 */
@AutoConfiguration
@EnableConfigurationProperties({
        EventStoreProperties.class,
        CqrsProperties.class,
        EventBusProperties.class,
        KingDataProperties.class,
        KingOssProperties.class,
        KingProperties.class,
        AppProperties.class,
})
@ComponentScan(basePackages = {
        "cn.treedeep.king.core.application.cqrs",
        "cn.treedeep.king.core.domain",
        "cn.treedeep.king.core.infrastructure",
        "cn.treedeep.king.shared"
}, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = cn.treedeep.king.core.infrastructure.audit.AggregateAuditAspect.class
        ),
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = cn.treedeep.king.core.infrastructure.eventstore.archive.EventArchiveService.class
        )
})
public class AggregateXAutoConfiguration {

    /**
     * 事件压缩器
     */
    @Bean
    @ConditionalOnMissingBean
    public EventCompressor eventCompressor(EventStoreProperties properties) {
        return new DefaultEventCompressor(properties.getBatchSize());
    }

    /**
     * 领域事件发布器
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainEventPublisher domainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DomainEventPublisher(applicationEventPublisher);
    }
}
