package cn.treedeep.king.shared.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * <p>
 * 封装常用的 Redis 操作方法，简化 Redis 操作
 * </p>
 *
 * @author Rubin
 * @version 2.2
 */
@Slf4j
@Component
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis 键接口
     */
    public interface IRedisKey {
        String getKey();

        default long getTimeout() {
            return 0;  // 过期时间(秒)，<=0表示不过期
        }
    }

    /* ------------------------- key 相关操作 ------------------------- */

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置key过期时间失败, key: {}", key, e);
            return false;
        }
    }

    public boolean expire(IRedisKey redisKey) {
        return expire(redisKey.getKey(), redisKey.getTimeout());
    }

    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("获取key过期时间失败, key: {}", key, e);
            return null;
        }
    }

    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("检查key是否存在失败, key: {}", key, e);
            return false;
        }
    }

    public boolean hasKey(IRedisKey key) {
        return hasKey(key.getKey());
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    public void del(IRedisKey... redisKeys) {
        if (redisKeys != null && redisKeys.length > 0) {
            if (redisKeys.length == 1) {
                redisTemplate.delete(redisKeys[0].getKey());
            } else {
                redisTemplate.delete(Arrays.stream(redisKeys)
                        .map(IRedisKey::getKey)
                        .toList());
            }
        }
    }

    /* ------------------------- String 相关操作 ------------------------- */

    public <T> Optional<T> get(String key) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            return o == null ? Optional.empty() : Optional.of((T) o);
        } catch (Exception e) {
            log.error("获取缓存失败, key: {}", key, e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> get(IRedisKey redisKey) {
        return get(redisKey.getKey());
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    public boolean set(IRedisKey redisKey, Object value) {
        boolean result = set(redisKey.getKey(), value);
        if (result && redisKey.getTimeout() > 0) {
            expire(redisKey.getKey(), redisKey.getTimeout());
        }
        return result;
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存(带过期时间)失败, key: {}", key, e);
            return false;
        }
    }

    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("递增操作失败, key: {}", key, e);
            return null;
        }
    }

    public Long incr(IRedisKey redisKey, long delta) {
        Long result = incr(redisKey.getKey(), delta);
        if (result != null && redisKey.getTimeout() > 0) {
            expire(redisKey.getKey(), redisKey.getTimeout());
        }
        return result;
    }

    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            log.error("递减操作失败, key: {}", key, e);
            return null;
        }
    }

    /* ------------------------- Hash 相关操作 ------------------------- */

    public <T> Optional<T> hget(String key, String item) {
        try {
            Object o = redisTemplate.opsForHash().get(key, item);
            return o == null ? Optional.empty() : Optional.of((T) o);
        } catch (Exception e) {
            log.error("获取hash缓存失败, key: {}, item: {}", key, item, e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> hget(IRedisKey redisKey, String item) {
        return hget(redisKey.getKey(), item);
    }

    public Map<Object, Object> hmget(String key) {
        try {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            return entries.isEmpty() ? Collections.emptyMap() : entries;
        } catch (Exception e) {
            log.error("获取hash所有缓存失败, key: {}", key, e);
            return Collections.emptyMap();
        }
    }

    public Map<Object, Object> hmget(IRedisKey redisKey) {
        return hmget(redisKey.getKey());
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("设置hash缓存失败, key: {}", key, e);
            return false;
        }
    }

    public boolean hmset(IRedisKey redisKey, Map<String, Object> map) {
        boolean result = hmset(redisKey.getKey(), map);
        if (result && redisKey.getTimeout() > 0) {
            expire(redisKey.getKey(), redisKey.getTimeout());
        }
        return result;
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置hash缓存(带过期时间)失败, key: {}", key, e);
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("设置hash项失败, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    public boolean hset(IRedisKey redisKey, String item, Object value) {
        boolean result = hset(redisKey.getKey(), item, value);
        if (result && redisKey.getTimeout() > 0) {
            expire(redisKey.getKey(), redisKey.getTimeout());
        }
        return result;
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置hash项(带过期时间)失败, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            log.error("删除hash项失败, key: {}, items: {}", key, Arrays.toString(item), e);
        }
    }

    public void hdel(IRedisKey redisKey, Object... item) {
        hdel(redisKey.getKey(), item);
    }

    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            log.error("检查hash项是否存在失败, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    public Double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            log.error("hash递增失败, key: {}, item: {}", key, item, e);
            return null;
        }
    }

    public Double hincr(IRedisKey redisKey, String item, double by) {
        Double result = hincr(redisKey.getKey(), item, by);
        if (result != null && redisKey.getTimeout() > 0) {
            expire(redisKey.getKey(), redisKey.getTimeout());
        }
        return result;
    }

    /* ------------------------- 分布式锁相关操作 ------------------------- */

    public boolean tryLock(IRedisKey lockKey, String requestId) {
        return tryLock(lockKey.getKey(), requestId, lockKey.getTimeout());
    }

    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("获取分布式锁失败, lockKey: {}, requestId: {}", lockKey, requestId, e);
            return false;
        }
    }

    public boolean releaseLock(String lockKey, String requestId) {
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long result = redisTemplate.execute(
                    (RedisCallback<Long>) connection -> connection.eval(
                            script.getBytes(),
                            ReturnType.INTEGER,
                            1,
                            lockKey.getBytes(),
                            requestId.getBytes()
                    )
            );
            return result != null && result == 1;
        } catch (Exception e) {
            log.error("释放分布式锁失败, lockKey: {}, requestId: {}", lockKey, requestId, e);
            return false;
        }
    }
}
