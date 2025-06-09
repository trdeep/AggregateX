package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;

/**
 * 业务约束验证器接口
 */
public interface BusinessConstraintValidator {
    /**
     * 验证聚合根状态变更是否符合业务约束
     *
     * @param event     触发状态变更的事件
     * @param aggregate 当前聚合根
     * @throws DomainValidationException 当验证失败时
     */
    void validateStateTransition(DomainEvent event, AggregateRoot<?> aggregate);

    /**
     * 验证业务规则
     *
     * @param event     要验证的事件
     * @param aggregate 相关的聚合根
     * @param rule      业务规则名称
     * @throws DomainValidationException 当验证失败时
     */
    void validateBusinessRule(DomainEvent event, AggregateRoot<?> aggregate, String rule);
}
