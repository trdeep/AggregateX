package cn.treedeep.king.core.infrastructure.eventbus;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.core.domain.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单事件总线实现
 * <p>
 * 使用内存中的ConcurrentHashMap来存储事件处理器
 */
public class SimpleEventBus implements EventBus {

    /**
     * 存储事件类型和对应的事件处理器列表
     * <p>
     * Key: 事件类型的完全限定名
     * Value: 该类型事件的处理器列表
     */
    private final Map<String, List<EventHandler<? extends DomainEvent>>> handlers = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void publish(DomainEvent event) {
        String eventType = event.getClass().getName();
        List<EventHandler<? extends DomainEvent>> eventHandlers = handlers.get(eventType);

        if (eventHandlers != null) {
            // 同步调用所有的事件处理器
            for (EventHandler<? extends DomainEvent> handler : eventHandlers) {
                ((EventHandler<DomainEvent>) handler).handle(event);
            }
        }
    }

    @Override
    public void subscribe(String eventType, EventHandler<?> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    @Override
    public void unsubscribe(String eventType, EventHandler<?> handler) {
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
        }
    }
}
