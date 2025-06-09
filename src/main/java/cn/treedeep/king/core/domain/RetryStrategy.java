package cn.treedeep.king.core.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

/**
 * 事件处理重试策略
 */
@Data
@Builder
public class RetryStrategy {

    // 最大重试次数
    private final int maxAttempts;

    // 初始重试延迟
    private final Duration initialDelay;

    // 最大重试延迟
    private final Duration maxDelay;

    // 退避乘数
    private final double backoffMultiplier;

    /**
     * 计算下一次重试延迟
     */
    public Duration nextDelay(int attempt) {
        double delayMillis = initialDelay.toMillis() * Math.pow(backoffMultiplier, attempt - 1);
        return Duration.ofMillis(Math.min(
            (long) delayMillis,
            maxDelay.toMillis()
        ));
    }

    /**
     * 创建指数退避策略
     */
    public static RetryStrategy exponentialBackoff() {
        return RetryStrategy.builder()
                .maxAttempts(3)
                .initialDelay(Duration.ofMillis(100))
                .maxDelay(Duration.ofSeconds(5))
                .backoffMultiplier(2.0)
                .build();
    }

    /**
     * 创建固定时间间隔策略
     */
    public static RetryStrategy fixedDelay(Duration delay, int maxAttempts) {
        return RetryStrategy.builder()
                .maxAttempts(maxAttempts)
                .initialDelay(delay)
                .maxDelay(delay)
                .backoffMultiplier(1.0)
                .build();
    }
}
