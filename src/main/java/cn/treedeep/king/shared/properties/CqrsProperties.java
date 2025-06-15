package cn.treedeep.king.shared.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CQRS配置属性类
 * <p>
 * 定义CQRS架构相关的所有配置项，支持通过application.yml或application.properties进行配置。
 * 配置前缀为'app.cqrs'，提供类型安全的配置绑定。
 * <p>
 * 主要配置域：
 * <ul>
 * <li>异步执行配置 - 控制命令异步处理的线程池参数</li>
 * <li>重试配置 - 定义命令执行失败时的重试策略</li>
 * <li>验证配置 - 控制命令和查询的验证行为</li>
 * <li>监控配置 - 配置性能指标收集和链路追踪</li>
 * </ul>
 * <p>
 * 配置示例：
 * <pre>{@code
 * app:
 *   cqrs:
 *     async:
 *       core-pool-size: 5
 *       max-pool-size: 10
 *       queue-capacity: 25
 *     retry:
 *       max-attempts: 3
 *       backoff-delay: 1000
 *     validation:
 *       enabled: true
 *       }
 * </pre>
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

    /**
     * 异步执行配置属性
     * <p>
     * 配置命令异步执行的线程池参数
     */
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

    /**
     * 重试配置属性
     * <p>
     * 配置命令执行失败时的重试策略参数
     */
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

    /**
     * 验证配置属性
     * <p>
     * 配置命令和查询的验证行为参数
     */
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
