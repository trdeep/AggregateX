package cn.treedeep.king.core.domain.eventsourcing;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventStore;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件溯源管理器
 * <p>
 * 负责:
 * 1. 从事件存储中恢复聚合根状态
 * 2. 管理事件的版本和顺序
 * 3. 处理并发冲突
 */
@Component
@RequiredArgsConstructor
public class EventSourcingManager {

    private final EventStore eventStore;

    /**
     * 从事件存储中加载聚合根
     */
    public <T extends AggregateRoot<ID>, ID extends Identifier> T load(ID id, Class<T> aggregateType) {
        try {
            // 获取历史事件
            List<DomainEvent> events = eventStore.getEvents(id.toString());

            // 创建聚合根实例
            T aggregate = aggregateType.getDeclaredConstructor().newInstance();

            // 按顺序重放事件
            for (DomainEvent event : events) {
                String methodName = "apply" + event.getClass().getSimpleName();
                aggregateType.getDeclaredMethod(methodName, event.getClass())
                        .invoke(aggregate, event);
            }

            // 清除重放的事件
            aggregate.clearDomainEvents();

            return aggregate;
        } catch (Exception e) {
            throw new EventSourcingException(
                String.format("Failed to load aggregate %s with id %s",
                    aggregateType.getSimpleName(),
                    id),
                e);
        }
    }

    /**
     * 保存聚合根产生的新事件
     */
    public <T extends AggregateRoot<ID>, ID extends Identifier> void save(T aggregate) {
        List<DomainEvent> newEvents = aggregate.getDomainEvents();
        if (newEvents.isEmpty()) {
            return;
        }

        String aggregateId = aggregate.getId().toString();
        eventStore.saveEvents(aggregateId, newEvents, aggregate.getVersion().intValue());

        // 清除已保存的事件
        aggregate.clearDomainEvents();
    }
}
