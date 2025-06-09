package cn.treedeep.king.core.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.stereotype.Component;

/**
 * 默认重试策略实现
 */
@Component
public class DefaultRetryStrategy implements RetryStrategy {

    private static final int MAX_ATTEMPTS = 3;
    private static final long INITIAL_DELAY = 1000L; // 1秒
    private static final long MAX_DELAY = 30000L;    // 30秒

    @Override
    public boolean shouldRetry(DomainEvent event, Exception e, int attempts) {
        // 如果是业务异常，不重试
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            return false;
        }

        return attempts < MAX_ATTEMPTS;
    }

    @Override
    public long getNextDelay(int attempts) {
        // 使用指数退避算法
        long delay = INITIAL_DELAY * (long) Math.pow(2, attempts - 1);
        return Math.min(delay, MAX_DELAY);
    }
}
