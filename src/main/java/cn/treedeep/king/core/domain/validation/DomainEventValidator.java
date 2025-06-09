package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.ValidationException;

import java.util.List;

/**
 * 领域事件验证器接口
 * <p>
 * 用于验证领域事件是否符合业务规则
 */
public interface DomainEventValidator<T extends DomainEvent> {
    /**
     * 验证领域事件是否符合业务规则
     *
     * @param event 要验证的事件
     * @throws ValidationException 当验证失败时
     */
    void validate(T event);

    void validateAll(List<T> events);
}
