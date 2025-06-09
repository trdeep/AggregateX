package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 默认事件序列验证器实现
 */
@Component
public class DefaultEventSequenceValidator {

    private final Map<String, Set<Class<? extends DomainEvent>>> eventSequences = new HashMap<>();

    /**
     * 验证事件序列
     *
     * @param events 要验证的事件列表
     * @throws DomainValidationException 当序列验证失败时
     */
    public void validateSequence(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        String aggregateId = events.get(0).getAggregateId();
        Set<Class<? extends DomainEvent>> processedEvents = new HashSet<>();

        for (DomainEvent event : events) {
            // 验证事件是否属于同一个聚合根
            if (!event.getAggregateId().equals(aggregateId)) {
                throw new DomainValidationException(
                    "INVALID_SEQUENCE",
                    "事件序列中包含不同聚合根的事件"
                );
            }

            // 验证事件顺序约束
            validateEventSequenceConstraints(event, processedEvents);

            // 添加到已处理事件集合
            processedEvents.add(event.getClass());
        }
    }

    private void validateEventSequenceConstraints(
            DomainEvent event,
            Set<Class<? extends DomainEvent>> processedEvents) {

        EventSequenceValidation annotation =
            event.getClass().getAnnotation(EventSequenceValidation.class);

        if (annotation != null) {
            // 验证必需的前置事件
            for (Class<? extends DomainEvent> requiredEvent : annotation.requiredPrecedingEvents()) {
                if (!processedEvents.contains(requiredEvent)) {
                    throw new DomainValidationException(
                        "MISSING_PRECEDING_EVENT",
                        String.format("事件%s缺少必需的前置事件%s",
                            event.getClass().getSimpleName(),
                            requiredEvent.getSimpleName())
                    );
                }
            }
        }
    }

    /**
     * 注册事件序列约束
     *
     * @param aggregateType 聚合根类型
     * @param eventSequence 事件序列
     */
    public void registerEventSequence(
            String aggregateType,
            Set<Class<? extends DomainEvent>> eventSequence) {
        eventSequences.put(aggregateType, new HashSet<>(eventSequence));
    }

    /**
     * 移除事件序列约束
     *
     * @param aggregateType 聚合根类型
     */
    public void removeEventSequence(String aggregateType) {
        eventSequences.remove(aggregateType);
    }

    /**
     * 清除所有事件序列约束
     */
    public void clearEventSequences() {
        eventSequences.clear();
    }

    /**
     * 获取已注册的事件序列
     *
     * @param aggregateType 聚合根类型
     * @return 事件序列集合
     */
    public Set<Class<? extends DomainEvent>> getEventSequence(String aggregateType) {
        return Collections.unmodifiableSet(
            eventSequences.getOrDefault(aggregateType, new HashSet<>())
        );
    }
}
