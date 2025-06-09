package cn.treedeep.king.core.infrastructure.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 分布式锁管理器
 * <p>
 * 提供基于Redis的分布式锁实现,用于:
 * 1. 聚合根并发访问控制
 * 2. 事务边界保护
 * 3. 集群环境下的资源互斥
 */
@Component
@RequiredArgsConstructor
public class DistributedLockManager {

    private final StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "lock:";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /**
     * 使用分布式锁执行操作
     *
     * @param lockKey 锁的键
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T executeLocked(String lockKey, Supplier<T> action) {
        return executeLocked(lockKey, DEFAULT_TIMEOUT, action);
    }

    /**
     * 使用指定超时时间的分布式锁执行操作
     *
     * @param lockKey 锁的键
     * @param timeout 锁的超时时间
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T executeLocked(String lockKey, Duration timeout, Supplier<T> action) {
        String redisKey = LOCK_PREFIX + lockKey;
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", timeout);

        if (Boolean.TRUE.equals(acquired)) {
            try {
                return action.get();
            } finally {
                redisTemplate.delete(redisKey);
            }
        } else {
            throw new LockAcquisitionException("无法获取锁: " + lockKey);
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, Duration timeout) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + lockKey, "1", timeout)
        );
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的键
     */
    public void unlock(String lockKey) {
        redisTemplate.delete(LOCK_PREFIX + lockKey);
    }

    /**
     * 生成聚合根锁键
     */
    public static String generateAggregateLockKey(String aggregateType, String aggregateId) {
        return String.format("aggregate:%s:%s", aggregateType, aggregateId);
    }
}
