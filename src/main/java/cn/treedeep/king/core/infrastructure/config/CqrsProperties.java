package cn.treedeep.king.core.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CQRS配置属性
 */
@Data
@ConfigurationProperties(prefix = "app.cqrs")
public class CqrsProperties {

    /**
     * 异步执行配置
     */
    private AsyncProperties async = new AsyncProperties();

    /**
     * 重试配置
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * 验证配置
     */
    private ValidationProperties validation = new ValidationProperties();

    @Data
    public static class AsyncProperties {
        /**
         * 核心线程池大小
         */
        private int corePoolSize = 5;

        /**
         * 最大线程池大小
         */
        private int maxPoolSize = 10;

        /**
         * 队列容量
         */
        private int queueCapacity = 25;

        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "async-command-";
    }

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

        /**
         * 重试延迟倍数
         */
        private double multiplier = 2.0;

        /**
         * 最大延迟时间（毫秒）
         */
        private long maxDelay = 10000;
    }

    @Data
    public static class ValidationProperties {
        /**
         * 是否启用快速失败模式
         */
        private boolean failFast = true;

        /**
         * 是否启用验证
         */
        private boolean validationEnabled = true;
    }
}
