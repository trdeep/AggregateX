package cn.treedeep.king.core.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 事件总线配置属性
 */
@Data
@ConfigurationProperties(prefix = "app.event-bus")
public class EventBusProperties {
    /**
     * 事件总线类型：simple（简单实现）或 rabbitmq（RabbitMQ实现）
     */
    private String type = "simple";

    /**
     * 是否启用异步处理
     */
    private boolean async = true;

    /**
     * 异步处理线程池大小
     */
    private int poolSize = 5;

    /**
     * 重试配置
     */
    private RetryProperties retry = new RetryProperties();

    @Data
    public static class RetryProperties {
        /**
         * 最大重试次数
         */
        private int maxAttempts = 3;

        /**
         * 初始重试延迟（毫秒）
         */
        private long initialDelay = 1000;
    }
}
