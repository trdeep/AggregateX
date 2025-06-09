package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.core.infrastructure.eventbus.SimpleEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 事件总线配置类
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(EventBusProperties.class)
public class EventBusConfiguration {

    private final EventBusProperties properties;

    /**
     * 配置事件处理线程池
     */
    @Bean
    public ThreadPoolTaskExecutor eventProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getPoolSize());
        executor.setMaxPoolSize(properties.getPoolSize());
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("event-processing-");
        executor.initialize();
        return executor;
    }

    /**
     * 配置事件总线实现
     */
    @Bean
    public EventBus eventBus(ThreadPoolTaskExecutor eventProcessingExecutor) {
        log.info("Configuring event bus implementation: {}", properties.getType());

        log.info("Event bus configured with retry policy: max attempts={}, initial delay={}ms",
                properties.getRetry().getMaxAttempts(),
                properties.getRetry().getInitialDelay());

        return switch (properties.getType().toLowerCase()) {
            case "simple" -> {
                log.info("Using simple event bus implementation with {} mode", properties.isAsync() ? "async" : "sync");
                yield new SimpleEventBus();
            }
            case "rabbitmq" -> {
                log.info("Using RabbitMQ event bus implementation");
                throw new UnsupportedOperationException("RabbitMQ implementation not yet available");
            }
            default -> throw new IllegalStateException(
                    "Unsupported event bus type: " + properties.getType() +
                            ". Supported types are: simple, rabbitmq"
            );
        };
    }
}
