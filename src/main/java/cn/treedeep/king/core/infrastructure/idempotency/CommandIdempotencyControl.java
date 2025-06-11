package cn.treedeep.king.core.infrastructure.idempotency;

import cn.treedeep.king.core.application.cqrs.command.Command;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 命令幂等性控制器
 * <p>
 * 基于Redis实现的命令幂等性控制，确保相同的命令不会被重复执行，
 * 这对于防止因网络重试、用户重复点击等原因导致的重复操作至关重要。
 * <p>
 * 实现原理：
 * <ul>
 * <li>使用Redis的SETNX操作实现分布式锁</li>
 * <li>基于命令ID生成唯一的幂等性键</li>
 * <li>设置合理的过期时间避免内存泄漏</li>
 * <li>支持自定义幂等性策略</li>
 * </ul>
 * <p>
 * 使用场景：
 * <ul>
 * <li>支付命令 - 防止重复扣款</li>
 * <li>订单创建 - 防止重复下单</li>
 * <li>状态变更 - 防止重复状态转换</li>
 * <li>外部API调用 - 防止重复调用第三方服务</li>
 * </ul>
 * <p>
 * 注意事项：
 * <ul>
 * <li>命令必须具有唯一且稳定的ID</li>
 * <li>过期时间需要根据业务场景合理设置</li>
 * <li>需要处理Redis不可用的降级策略</li>
 * </ul>
 */
@Component
public class CommandIdempotencyControl {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String IDEMPOTENCY_KEY_PREFIX = "cmd_idempotency:";
    private static final long DEFAULT_EXPIRATION = 24 * 60 * 60; // 24小时

    /**
     * 构造幂等性控制器
     *
     * @param redisTemplate Redis模板，用于与Redis交互
     */
    public CommandIdempotencyControl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 检查命令是否重复执行
     *
     * @param command 要检查的命令
     * @return 如果是重复命令返回true，否则返回false
     */
    public boolean isDuplicate(Command command) {
        String key = buildKey(command);
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, String.valueOf(System.currentTimeMillis()),
                        DEFAULT_EXPIRATION, TimeUnit.SECONDS);
        return success != null && !success;
    }

    /**
     * 生成幂等性键
     *
     * @param command 命令对象
     * @return Redis键
     */
    private String buildKey(Command command) {
        return IDEMPOTENCY_KEY_PREFIX +
               command.getClass().getSimpleName() + ":" +
               command.getCommandId();
    }

    /**
     * 清除幂等性记录
     *
     * @param command 命令对象
     */
    public void clearIdempotencyRecord(Command command) {
        String key = buildKey(command);
        redisTemplate.delete(key);
    }
}
