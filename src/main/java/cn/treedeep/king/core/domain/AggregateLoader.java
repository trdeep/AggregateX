package cn.treedeep.king.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 聚合根恢复器
 * <p>
 * 用于从事件存储中恢复聚合根的状态。
 * 实现了事件溯源（Event Sourcing）模式，通过重放历史事件来重建聚合根状态。
 * <p>
 * 功能特点：<br>
 * 1. 自动加载并按顺序重放领域事件<br>
 * 2. 使用反射机制调用事件处理方法<br>
 * 3. 支持任意类型的聚合根<br>
 * 4. 异常处理和状态验证
 * <p>
 * 使用约定：<br>
 * 1. 聚合根类必须提供无参构造函数<br>
 * 2. 事件处理方法必须遵循命名规范：apply + 事件类名<br>
 * 3. 事件处理方法必须是实例方法（非静态）<br>
 * 4. 事件必须按照正确的顺序保存和重放
 * <p>
 * 示例用法：
 * <pre>
 * {@code
 * Order order = aggregateLoader.load("order-123", Order.class);
 * }
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class AggregateLoader {

    private final EventStore eventStore;

    /**
     * 从事件存储中加载聚合根
     *
     * @param aggregateId   聚合根ID
     * @param aggregateType 聚合根类型
     * @param <T>           聚合根类型
     * @return 重建后的聚合根实例
     */
    public <T extends AggregateRoot<?>> T load(String aggregateId, Class<T> aggregateType) {
        try {
            // 创建新的聚合根实例
            T aggregate = aggregateType.getDeclaredConstructor().newInstance();

            // 获取该聚合根的所有历史事件
            List<DomainEvent> events = eventStore.getEvents(aggregateId);

            // 按照事件发生的顺序重放事件
            events.forEach(event -> {
                // 使用反射调用事件处理方法
                try {
                    String methodName = "apply" + event.getClass().getSimpleName();
                    aggregateType.getDeclaredMethod(methodName, event.getClass())
                            .invoke(aggregate, event);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to apply event: " + event.getClass().getSimpleName(), e);
                }
            });

            // 清除领域事件，因为这些都是历史事件
            aggregate.clearDomainEvents();

            return aggregate;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load aggregate: " + aggregateId, e);
        }
    }
}
