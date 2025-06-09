package cn.treedeep.king.core.infrastructure.idempotency;

import cn.treedeep.king.core.application.cqrs.command.Command;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 命令幂等性控制器
 * <p>
 * 使用Redis实现命令的幂等性控制，防止重复执行相同的命令
 */
@Component
public class CommandIdempotencyControl {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String IDEMPOTENCY_KEY_PREFIX = "cmd_idempotency:";
    private static final long DEFAULT_EXPIRATION = 24 * 60 * 60; // 24小时

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
