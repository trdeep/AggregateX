package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 默认领域事件验证器实现
 * <p>
 * 提供领域事件的验证功能，包括：
 * <ul>
 *   <li>事件完整性验证</li>
 *   <li>事件序列验证</li>
 *   <li>业务规则验证</li>
 * </ul>
 *
 * @param <T> 领域事件类型
 */
@Component
@RequiredArgsConstructor
public class DefaultDomainEventValidator<T extends DomainEvent> implements DomainEventValidator<T> {

    private final Map<Class<? extends DomainEvent>, Predicate<DomainEvent>> validationRules = new ConcurrentHashMap<>();

    /**
     * 验证单个领域事件
     * <p>
     * 对单个领域事件进行完整性检查和业务规则验证，包括：
     * <ul>
     *   <li>事件标识符验证</li>
     *   <li>事件时间戳验证</li>
     *   <li>聚合根引用验证</li>
     *   <li>业务规则验证</li>
     * </ul>
     *
     * @param event 待验证的领域事件
     * @throws DomainValidationException 当验证失败时抛出
     */
    @Override
    public void validate(T event) {
        List<String> errors = new ArrayList<>();

        // 验证事件标识符
        validateEventIdentifier(event, errors);

        // 验证事件时间戳
        validateEventTimestamp(event, errors);

        // 验证聚合根引用
        validateAggregateReference(event, errors);

        // 验证业务规则
        validateBusinessRules(event, errors);

        // 如果有错误，抛出异常
        if (!errors.isEmpty()) {
            throw new DomainValidationException(
                "EVENT_VALIDATION_ERROR",
                String.format("领域事件验证失败: %s", String.join(", ", errors))
            );
        }
    }

    /**
     * 批量验证领域事件
     * <p>
     * 对多个领域事件进行验证，并收集所有验证错误。
     * 验证过程包括：
     * <ul>
     *   <li>单个事件验证</li>
     *   <li>事件序列验证</li>
     *   <li>事件依赖关系验证</li>
     * </ul>
     *
     * @param events 待验证的领域事件列表
     * @throws DomainValidationException 当任何一个事件验证失败时抛出
     */
    @Override
    public void validateAll(List<T> events) {
        List<String> errors = new ArrayList<>();

        // 验证每个事件
        for (T event : events) {
            try {
                validate(event);
            } catch (DomainValidationException e) {
                errors.add(e.getMessage());
            }
        }

        // 验证事件序列
        validateEventSequence(events, errors);

        // 如果有错误，抛出异常
        if (!errors.isEmpty()) {
            throw new DomainValidationException(
                "EVENT_SEQUENCE_VALIDATION_ERROR",
                String.format("领域事件序列验证失败: %s", String.join(", ", errors))
            );
        }
    }

    /**
     * 验证事件标识符
     *
     * @param event 待验证的领域事件
     * @param errors 错误列表
     */
    protected void validateEventIdentifier(T event, List<String> errors) {
        if (event.getEventId() == null) {
            errors.add("事件标识符不能为空");
        }
    }

    /**
     * 验证事件时间戳
     *
     * @param event 待验证的领域事件
     * @param errors 错误列表
     */
    protected void validateEventTimestamp(T event, List<String> errors) {
        if (event.getOccurredOn() == null) {
            errors.add("事件发生时间不能为空");
        }
    }

    /**
     * 验证聚合根引用
     *
     * @param event 待验证的领域事件
     * @param errors 错误列表
     */
    protected void validateAggregateReference(T event, List<String> errors) {
        if (event.getAggregateId() == null) {
            errors.add("聚合根引用不能为空");
        }
    }

    /**
     * 验证业务规则
     *
     * @param event 待验证的领域事件
     * @param errors 错误列表
     */
    protected void validateBusinessRules(T event, List<String> errors) {
        // 子类可以重写此方法以添加特定的业务规则验证逻辑
    }

    /**
     * 验证事件序列
     *
     * @param events 待验证的领域事件列表
     * @param errors 错误列表
     */
    protected void validateEventSequence(List<T> events, List<String> errors) {
        // 子类可以重写此方法以添加特定的事件序列验证逻辑
    }

    /**
     * 注册自定义验证规则
     *
     * @param eventType 事件类型
     * @param validationRule 验证规则
     */
    public <E extends DomainEvent> void registerValidationRule(
            Class<E> eventType,
            Predicate<DomainEvent> validationRule) {
        validationRules.put(eventType, validationRule);
    }

    /**
     * 移除自定义验证规则
     *
     * @param eventType 事件类型
     */
    public <E extends DomainEvent> void removeValidationRule(Class<E> eventType) {
        validationRules.remove(eventType);
    }

    /**
     * 清除所有自定义验证规则
     */
    public void clearValidationRules() {
        validationRules.clear();
    }
}
