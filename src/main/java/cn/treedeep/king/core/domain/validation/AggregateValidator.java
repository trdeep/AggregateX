package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;

/**
 * 聚合根验证器接口
 *
 * @param <T> 聚合根类型
 */
public interface AggregateValidator<T extends AggregateRoot<?>> {
    /**
     * 验证聚合根状态
     *
     * @param aggregate 要验证的聚合根
     * @param event     触发验证的事件
     * @throws DomainValidationException 当验证失败时
     */
    void validateAggregateState(T aggregate, DomainEvent event);

    /**
     * 验证聚合根不变量
     *
     * @param aggregate 要验证的聚合根
     * @throws DomainValidationException 当验证失败时
     */
    void validateInvariants(T aggregate);
}
