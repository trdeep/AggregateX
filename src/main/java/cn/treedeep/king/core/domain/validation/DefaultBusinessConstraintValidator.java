package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认业务约束验证器实现
 */
@Component
@RequiredArgsConstructor
public class DefaultBusinessConstraintValidator implements BusinessConstraintValidator {

    private final ValidationProperties properties;

    @Override
    public void validateStateTransition(DomainEvent event, AggregateRoot<?> aggregate) {
        // 1. 检查聚合根状态转换的合法性
        validateAggregateVersion(event, aggregate);

        // 2. 检查事件数量限制
        validateEventCount(aggregate);

        // 3. 在严格模式下执行额外的验证
        if (properties.isStrictMode()) {
            validateStrictModeConstraints(event, aggregate);
        }
    }

    @Override
    public void validateBusinessRule(DomainEvent event, AggregateRoot<?> aggregate, String rule) {
        ValidationProperties.BusinessRuleConfig ruleConfig = properties.getBusinessRules().get(rule);
        if (ruleConfig == null || !ruleConfig.isEnabled()) {
            return;
        }

        String errorMessage = ruleConfig.getErrorMessage();
        Map<String, Object> context = new HashMap<>();
        context.put("aggregateId", aggregate.getId());
        context.put("eventType", event.getClass().getSimpleName());
        context.put("rule", rule);

        // 执行具体的业务规则验证
        if (!validateRule(rule, event, aggregate)) {
            throw new DomainValidationException(
                rule,
                errorMessage != null ? errorMessage : "业务规则验证失败: " + rule,
                context,
                ruleConfig.getSeverity()
            );
        }
    }

    private void validateAggregateVersion(DomainEvent event, AggregateRoot<?> aggregate) {
        if (event.getAggregateVersion() <= aggregate.getVersion()) {
            throw new DomainValidationException(
                "VERSION_CONFLICT",
                String.format("事件版本(%d)不能小于或等于聚合根当前版本(%d)",
                    event.getAggregateVersion(), aggregate.getVersion())
            );
        }
    }

    private void validateEventCount(AggregateRoot<?> aggregate) {
        if (aggregate.getDomainEvents().size() >= properties.getMaxEventsPerAggregate()) {
            throw new DomainValidationException(
                "MAX_EVENTS_EXCEEDED",
                String.format("聚合根事件数量超过限制(%d)",
                    properties.getMaxEventsPerAggregate())
            );
        }
    }

    private void validateStrictModeConstraints(DomainEvent event, AggregateRoot<?> aggregate) {
        // 在严格模式下执行更严格的验证
        // 这里可以添加更多的验证规则
    }

    private boolean validateRule(String rule, DomainEvent event, AggregateRoot<?> aggregate) {
        // 这里实现具体的业务规则验证逻辑
        // 可以通过策略模式或其他方式来组织和扩展规则
        return true; // 默认返回true，实际实现需要根据具体规则来判断
    }
}
