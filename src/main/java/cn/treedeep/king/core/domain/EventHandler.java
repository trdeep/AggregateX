package cn.treedeep.king.core.domain;

/**
 * 事件处理器接口
 * <p>
 * 负责处理特定类型的领域事件
 * 事件处理器应该是幂等的，因为同一个事件可能会被多次投递
 *
 * @param <T> 要处理的领域事件类型
 */
public interface EventHandler<T extends DomainEvent> {

    /**
     * 处理领域事件
     * <p>
     * 实现类需要保证处理逻辑的幂等性
     *
     * @param event 要处理的领域事件
     */
    void handle(T event);
}
