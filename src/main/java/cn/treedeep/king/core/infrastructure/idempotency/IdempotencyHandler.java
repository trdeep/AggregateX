package cn.treedeep.king.core.infrastructure.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 幂等性处理器
 * <p>
 * 确保相同的领域事件不会被重复处理。使用Redis实现分布式锁和幂等性检查。
 */
@Component
@RequiredArgsConstructor
public class IdempotencyHandler {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "idempotency:";
    private static final Duration DEFAULT_EXPIRATION = Duration.ofHours(24);

    /**
     * 执行幂等操作
     *
     * @param key 幂等键
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T execute(String key, Supplier<T> action) {
        String redisKey = KEY_PREFIX + key;

        // 尝试获取分布式锁
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", DEFAULT_EXPIRATION);
        if (Boolean.TRUE.equals(acquired)) {
            try {
                return action.get();
            } finally {
                // 操作完成后删除锁
                redisTemplate.delete(redisKey);
            }
        } else {
            throw new IllegalStateException("重复的操作: " + key);
        }
    }

    /**
     * 判断操作是否已执行
     */
    public boolean isProcessed(String key) {
        return redisTemplate.hasKey(KEY_PREFIX + key);
    }

    /**
     * 标记操作已执行
     */
    public void markAsProcessed(String key) {
        redisTemplate.opsForValue().set(
            KEY_PREFIX + key,
            "1",
            DEFAULT_EXPIRATION
        );
    }
}
