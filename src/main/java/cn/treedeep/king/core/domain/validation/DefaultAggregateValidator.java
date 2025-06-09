package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 默认聚合根验证器实现
 */
@Component
public class DefaultAggregateValidator<T extends AggregateRoot<?>> implements AggregateValidator<T> {

    private final Map<Class<?>, Map<String, Predicate<T>>> invariantRules = new HashMap<>();
    private final ValidationProperties properties;

    public DefaultAggregateValidator(ValidationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void validateAggregateState(T aggregate, DomainEvent event) {
        // 1. 验证聚合根基本完整性
        validateAggregateCompleteness(aggregate);

        // 2. 验证事件与聚合根的关联
        validateEventAggregateAssociation(aggregate, event);

        // 3. 验证版本一致性
        validateVersionConsistency(aggregate, event);

        // 4. 验证业务状态
        validateBusinessState(aggregate, event);
    }

    @Override
    public void validateInvariants(T aggregate) {
        Map<String, Predicate<T>> rules = invariantRules.get(aggregate.getClass());
        if (rules != null) {
            rules.forEach((ruleName, rule) -> {
                if (!rule.test(aggregate)) {
                    throw new DomainValidationException(
                        "INVARIANT_VIOLATION",
                        String.format("聚合根%s违反了不变量规则: %s",
                            aggregate.getClass().getSimpleName(),
                            ruleName)
                    );
                }
            });
        }
    }

    private void validateAggregateCompleteness(T aggregate) {
        if (aggregate == null) {
            throw new DomainValidationException("NULL_AGGREGATE", "聚合根不能为空");
        }
        if (aggregate.getId() == null) {
            throw new DomainValidationException("MISSING_AGGREGATE_ID", "聚合根ID不能为空");
        }
    }

    private void validateEventAggregateAssociation(T aggregate, DomainEvent event) {
        if (!event.getAggregateId().equals(aggregate.getId().toString())) {
            throw new DomainValidationException(
                "INVALID_EVENT_AGGREGATE_ASSOCIATION",
                String.format("事件(ID=%s)与聚合根(ID=%s)不匹配",
                    event.getAggregateId(),
                    aggregate.getId())
            );
        }
    }

    private void validateVersionConsistency(T aggregate, DomainEvent event) {
        if (event.getAggregateVersion() <= aggregate.getVersion()) {
            throw new DomainValidationException(
                "VERSION_CONFLICT",
                String.format("事件版本(%d)不能小于或等于聚合根当前版本(%d)",
                    event.getAggregateVersion(),
                    aggregate.getVersion())
            );
        }
    }

    private void validateBusinessState(T aggregate, DomainEvent event) {
        // 在这里实现具体的业务状态验证逻辑
        // 可以根据不同的聚合根类型和事件类型执行不同的验证
    }

    /**
     * 注册聚合根不变量规则
     *
     * @param aggregateType 聚合根类型
     * @param ruleName 规则名称
     * @param rule 规则实现
     */
    public void registerInvariantRule(
            Class<T> aggregateType,
            String ruleName,
            Predicate<T> rule) {
        invariantRules
            .computeIfAbsent(aggregateType, k -> new HashMap<>())
            .put(ruleName, rule);
    }

    /**
     * 移除聚合根不变量规则
     *
     * @param aggregateType 聚合根类型
     * @param ruleName 规则名称
     */
    public void removeInvariantRule(Class<T> aggregateType, String ruleName) {
        Map<String, Predicate<T>> rules = invariantRules.get(aggregateType);
        if (rules != null) {
            rules.remove(ruleName);
        }
    }

    /**
     * 清除所有不变量规则
     */
    public void clearInvariantRules() {
        invariantRules.clear();
    }
}
