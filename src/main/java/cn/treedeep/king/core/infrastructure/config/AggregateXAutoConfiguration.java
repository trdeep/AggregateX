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
 * Spring Boot自动配置类，负责初始化AggregateX框架的所有核心组件。
 * 通过Spring Boot的自动配置机制，用户只需要添加框架依赖即可自动获得完整的DDD功能。
 * <p>
 * 自动配置的组件包括：
 * <ul>
 * <li>CQRS组件 - 命令总线、查询总线、处理器注册</li>
 * <li>事件存储 - 事件存储、事件压缩、事件归档</li>
 * <li>领域事件 - 事件发布器、事件总线</li>
 * <li>仓储 - 抽象仓储实现、缓存支持</li>
 * <li>验证组件 - 聚合根验证、事件验证</li>
 * <li>监控组件 - 性能监控、审计日志</li>
 * </ul>
 * <p>
 * 配置属性：
 * <ul>
 * <li>{@link EventStoreProperties} - 事件存储配置</li>
 * <li>{@link CqrsProperties} - CQRS组件配置</li>
 * <li>{@link EventBusProperties} - 事件总线配置</li>
 * <li>{@link AppProperties} - 应用通用配置</li>
 * </ul>
 * <p>
 * 组件扫描：
 * <ul>
 * <li>自动扫描并注册所有CQRS处理器</li>
 * <li>自动配置领域服务和基础设施组件</li>
 * <li>排除某些需要条件配置的组件</li>
 * </ul>
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
     * <p>
     * 用于压缩事件存储中的事件数据，减少存储空间占用
     *
     * @param properties 事件存储配置属性
     * @return 事件压缩器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public EventCompressor eventCompressor(EventStoreProperties properties) {
        return new DefaultEventCompressor(properties.getBatchSize());
    }

    /**
     * 领域事件发布器
     * <p>
     * 负责发布领域事件到Spring事件系统
     *
     * @param applicationEventPublisher Spring应用事件发布器
     * @return 领域事件发布器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainEventPublisher domainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DomainEventPublisher(applicationEventPublisher);
    }
}
