package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认上下文感知验证器实现
 */
@Component
public class DefaultContextAwareValidator<T extends DomainEvent> implements ContextAwareValidator<T> {

    private final Map<String, Object> validationContextCache = new HashMap<>();

    @Override
    public void validateWithContext(T event, DomainContext context) {
        // 1. 验证上下文完整性
        validateContextCompleteness(context);

        // 2. 基于上下文验证事件
        validateEventWithContext(event, context);

        // 3. 缓存验证结果
        cacheValidationResult(event, context);
    }

    private void validateContextCompleteness(DomainContext context) {
        if (context == null) {
            throw new DomainValidationException(
                "MISSING_CONTEXT",
                "验证上下文不能为空"
            );
        }
    }

    private void validateEventWithContext(T event, DomainContext context) {
        // 在这里实现具体的基于上下文的验证逻辑
        // 例如：验证事件是否符合当前上下文的业务状态
        Object currentState = context.get("currentState");
        if (currentState != null) {
            validateStateTransition(event, currentState);
        }

        // 验证事件是否满足上下文中的业务规则
        Object businessRules = context.get("businessRules");
        if (businessRules != null) {
            validateBusinessRules(event, businessRules);
        }
    }

    private void validateStateTransition(T event, Object currentState) {
        // 实现状态转换验证逻辑
    }

    private void validateBusinessRules(T event, Object businessRules) {
        // 实现业务规则验证逻辑
    }

    private void cacheValidationResult(T event, DomainContext context) {
        String cacheKey = generateCacheKey(event);
        validationContextCache.put(cacheKey, context);
    }

    private String generateCacheKey(T event) {
        return event.getAggregateId() + ":" + event.getClass().getSimpleName();
    }

    /**
     * 清除验证缓存
     */
    public void clearValidationCache() {
        validationContextCache.clear();
    }

    /**
     * 获取已缓存的验证上下文
     *
     * @param event 领域事件
     * @return 验证上下文
     */
    public Object getCachedValidationContext(T event) {
        return validationContextCache.get(generateCacheKey(event));
    }
}
