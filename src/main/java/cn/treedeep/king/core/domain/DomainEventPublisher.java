package cn.treedeep.king.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件发布者基类
 * <p>
 * 提供事件发布的通用功能:
 * 1. 事务内发布
 * 2. 事务提交后发布
 * 3. 批量发布支持
 */
@Component
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final ThreadLocal<List<DomainEvent>> pendingEvents = ThreadLocal.withInitial(ArrayList::new);

    /**
     * 立即发布事件
     */
    public void publish(DomainEvent event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 如果在事务中,则将事件添加到待发布列表
            pendingEvents.get().add(event);

            // 注册事务同步器,在事务提交后发布事件
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishEvent(event);
                }

                @Override
                public void afterCompletion(int status) {
                    // 清理线程本地变量
                    pendingEvents.remove();
                }
            });
        } else {
            // 不在事务中,直接发布
            publishEvent(event);
        }
    }

    /**
     * 批量发布事件
     */
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    private void publishEvent(DomainEvent event) {
        try {
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            // TODO: 处理发布失败的情况,可以:
            // 1. 重试
            // 2. 记录到失败队列
            // 3. 告警通知
            throw new EventPublishException("Failed to publish event: " + event.getClass().getSimpleName(), e);
        }
    }

    /**
     * 获取当前事务中待发布的事件
     */
    public List<DomainEvent> getPendingEvents() {
        return new ArrayList<>(pendingEvents.get());
    }

    /**
     * 清除待发布事件
     */
    public void clearPendingEvents() {
        pendingEvents.remove();
    }
}
