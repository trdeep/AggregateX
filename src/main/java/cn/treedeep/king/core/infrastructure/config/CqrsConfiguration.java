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
 * CQRS基础设施配置
 * <p>
 * 集中配置CQRS相关的基础组件，包括：
 * 1. 异步处理支持
 * 2. 命令验证器
 * 3. 监控指标收集
 * 4. 重试机制
 */
@Configuration
@EnableAsync
@EnableRetry
@EnableConfigurationProperties(CqrsProperties.class)
public class CqrsConfiguration {

    private final CqrsProperties properties;

    public CqrsConfiguration(CqrsProperties properties) {
        this.properties = properties;
    }

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

    @Bean
    public CommandValidator commandValidator(Validator validator) {
        CommandValidator commandValidator = new CommandValidator(validator);
        commandValidator.setFailFast(properties.getValidation().isFailFast());
        commandValidator.setValidationEnabled(properties.getValidation().isValidationEnabled());
        return commandValidator;
    }

    @Bean
    public CommandMetrics commandMetrics(MeterRegistry meterRegistry) {
        return new CommandMetrics(meterRegistry);
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
