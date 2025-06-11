package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.domain.validation.CommandValidator;
import cn.treedeep.king.core.infrastructure.monitoring.CommandMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Validator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.concurrent.Executor;

/**
 * CQRS基础设施配置类
 * <p>
 * 负责配置CQRS架构所需的核心基础设施组件，提供命令查询分离的完整运行环境。
 * <p>
 * 配置的组件：
 * <ul>
 * <li>异步执行器 - 支持命令的异步处理</li>
 * <li>命令验证器 - 基于JSR-303的命令参数验证</li>
 * <li>监控指标收集器 - 收集命令执行的性能指标</li>
 * <li>重试机制 - 支持失败命令的自动重试</li>
 * <li>线程池配置 - 优化并发处理性能</li>
 * </ul>
 * <p>
 * 关键特性：
 * <ul>
 * <li>@EnableAsync - 启用Spring的异步处理能力</li>
 * <li>@EnableRetry - 启用声明式重试机制</li>
 * <li>@EnableConfigurationProperties - 绑定配置属性</li>
 * <li>线程池隔离 - 命令执行使用独立的线程池</li>
 * </ul>
 * <p>
 * 配置项：
 * <ul>
 * <li>cqrs.async.core-pool-size - 核心线程数</li>
 * <li>cqrs.async.max-pool-size - 最大线程数</li>
 * <li>cqrs.async.queue-capacity - 队列容量</li>
 * <li>cqrs.validation.enabled - 是否启用验证</li>
 * </ul>
 */
@Configuration
@EnableAsync
@EnableRetry
@EnableConfigurationProperties(CqrsProperties.class)
public class CqrsConfiguration {

    private final CqrsProperties properties;

    /**
     * 构造CQRS配置
     *
     * @param properties CQRS配置属性
     */
    public CqrsConfiguration(CqrsProperties properties) {
        this.properties = properties;
    }

    /**
     * 配置命令执行线程池
     *
     * @return 配置好的线程池执行器
     */
    @Bean(name = "commandExecutor")
    public Executor commandExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(properties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(properties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix(properties.getAsync().getThreadNamePrefix());
        executor.initialize();
        return executor;
    }

    /**
     * 配置命令验证器
     *
     * @param validator JSR-303验证器
     * @return 配置好的命令验证器
     */
    @Bean
    public CommandValidator commandValidator(Validator validator) {
        CommandValidator commandValidator = new CommandValidator(validator);
        commandValidator.setFailFast(properties.getValidation().isFailFast());
        commandValidator.setValidationEnabled(properties.getValidation().isValidationEnabled());
        return commandValidator;
    }

    /**
     * 配置命令指标收集器
     *
     * @param meterRegistry 指标注册表
     * @return 配置好的命令指标收集器
     */
    @Bean
    public CommandMetrics commandMetrics(MeterRegistry meterRegistry) {
        return new CommandMetrics(meterRegistry);
    }

    /**
     * 配置JSR-303验证器
     *
     * @return 配置好的验证器
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
