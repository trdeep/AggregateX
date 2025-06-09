package cn.treedeep.king.core.infrastructure.eventstore;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的事件存储实现
 * <p>
 * 主要用于开发和测试环境，数据存储在内存中，重启后数据会丢失
 */
public class InMemoryEventStore implements EventStore {
    private final Map<String, List<DomainEvent>> eventStream = new ConcurrentHashMap<>();
    private final List<DomainEvent> allEvents = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void saveEvents(String aggregateId, List<DomainEvent> events, int expectedVersion) {
        List<DomainEvent> streamEvents = eventStream.computeIfAbsent(aggregateId, k -> new ArrayList<>());

        synchronized (streamEvents) {
            if (streamEvents.size() != expectedVersion) {
                throw new RuntimeException("并发冲突：预期版本号不匹配");
            }

            streamEvents.addAll(events);
            allEvents.addAll(events);
        }
    }

    @Override
    public List<DomainEvent> getEvents(String aggregateId) {
        return new ArrayList<>(eventStream.getOrDefault(aggregateId, new ArrayList<>()));
    }

    @Override
    public List<DomainEvent> getAllEvents() {
        return new ArrayList<>(allEvents);
    }
}
