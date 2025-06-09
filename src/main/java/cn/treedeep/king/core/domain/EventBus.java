package cn.treedeep.king.core.domain;

/**
 * 事件总线接口
 * <p>
 * 用于发布和订阅领域事件
 */
public interface EventBus {

    /**
     * 发布领域事件
     *
     * @param event 要发布的领域事件
     */
    void publish(DomainEvent event);

    /**
     * 订阅特定类型的领域事件
     *
     * @param eventType 事件类型的完全限定名
     * @param handler   事件处理器
     */
    void subscribe(String eventType, EventHandler<?> handler);

    /**
     * 取消订阅特定类型的领域事件
     *
     * @param eventType 事件类型的完全限定名
     * @param handler   要取消的事件处理器
     */
    void unsubscribe(String eventType, EventHandler<?> handler);
}
