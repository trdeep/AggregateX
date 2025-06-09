package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证配置类
 * <p>
 * 提供领域验证相关的配置，包括：
 * <ul>
 *   <li>验证器配置</li>
 *   <li>验证规则配置</li>
 *   <li>验证属性配置</li>
 * </ul>
 */
@Configuration
@EnableConfigurationProperties(ValidationProperties.class)
public class ValidationConfiguration {

    @Bean
    public EventIntegrityChecker eventIntegrityChecker(ValidationProperties properties) {
        return new DefaultEventIntegrityChecker();
    }

    @Bean
    public CustomValidationRegistry customValidationRegistry() {
        return new CustomValidationRegistry();
    }

    /**
     * 创建领域事件验证器
     * <p>
     * 配置并创建用于验证领域事件的验证器实例。
     * 验证器将根据配置的属性进行初始化。
     *
     * @return 领域事件验证器实例
     */
    @Bean
    public <T extends DomainEvent> DomainEventValidator<T> domainEventValidator() {
        return new DefaultDomainEventValidator<>();
    }

    /**
     * 创建聚合根验证器
     * <p>
     * 配置并创建用于验证聚合根的验证器实例。
     * 验证器将根据配置的属性进行初始化。
     *
     * @param properties 验证配置属性
     * @return 聚合根验证器实例
     */
    @Bean
    public <T extends AggregateRoot<?>> AggregateValidator<T> aggregateValidator(
            ValidationProperties properties) {
        return new DefaultAggregateValidator<>(properties);
    }

    /**
     * 创建上下文感知验证器
     * <p>
     * 配置并创建支持上下文感知的验证器实例。
     * 验证器将根据配置的属性进行初始化。
     *
     * @return 上下文感知验证器实例
     */
    @Bean
    public ContextAwareValidator<?> contextAwareValidator() {
        return new DefaultContextAwareValidator<>();
    }

    @Bean
    public DefaultEventSequenceValidator eventSequenceValidator() {
        return new DefaultEventSequenceValidator();
    }
}
