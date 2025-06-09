package cn.treedeep.king.core.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;

/**
 * 事件重试策略接口
 */
public interface RetryStrategy {
    /**
     * 判断是否应该重试
     *
     * @param event    发生异常的事件
     * @param e        发生的异常
     * @param attempts 已经尝试的次数
     * @return 是否应该重试
     */
    boolean shouldRetry(DomainEvent event, Exception e, int attempts);

    /**
     * 获取下一次重试的延迟时间（毫秒）
     *
     * @param attempts 已经尝试的次数
     * @return 下一次重试前需要等待的时间（毫秒）
     */
    long getNextDelay(int attempts);
}
