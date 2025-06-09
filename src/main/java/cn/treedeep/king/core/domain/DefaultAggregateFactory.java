package cn.treedeep.king.core.domain;

import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Identifier;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 默认聚合根工厂实现
 *
 * @param <T> 聚合根类型
 * @param <ID> 聚合根标识符类型
 */
@RequiredArgsConstructor
public class DefaultAggregateFactory<T extends AggregateRoot<ID>, ID extends Identifier> implements AggregateFactory<T, ID> {

    private final Class<T> aggregateType;
    private final EventBus eventBus;

    @Override
    public T create(ID id) {
        try {
            Constructor<T> constructor = aggregateType.getDeclaredConstructor(id.getClass());
            return constructor.newInstance(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create aggregate: " + aggregateType.getSimpleName(), e);
        }
    }

    @Override
    public T reconstitute(ID id, List<DomainEvent> events) {
        // 1. 创建新实例
        T aggregate = create(id);

        // 2. 按顺序重放所有事件
        for (DomainEvent event : events) {
            try {
                String methodName = "apply" + event.getClass().getSimpleName();
                aggregateType.getDeclaredMethod(methodName, event.getClass())
                        .invoke(aggregate, event);
            } catch (Exception e) {
                throw new RuntimeException(
                    String.format("Failed to apply event %s to aggregate %s",
                        event.getClass().getSimpleName(),
                        aggregateType.getSimpleName()),
                    e);
            }
        }

        // 3. 清除已重放的事件
        aggregate.clearDomainEvents();

        return aggregate;
    }
}
