package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.ValidationException;

import java.util.List;

/**
 * 领域事件验证器接口
 * <p>
 * 用于验证领域事件是否符合业务规则和结构完整性。
 * 实现此接口可以为特定类型的领域事件提供自定义验证逻辑。
 *
 * @param <T> 领域事件类型
 */
public interface DomainEventValidator<T extends DomainEvent> {
    /**
     * 验证单个领域事件是否符合业务规则
     * <p>
     * 验证内容包括：
     * <ul>
     * <li>事件基本结构完整性</li>
     * <li>事件数据有效性</li>
     * <li>业务规则符合性</li>
     * </ul>
     *
     * @param event 要验证的事件
     * @throws ValidationException 当验证失败时抛出异常
     */
    void validate(T event);

    /**
     * 批量验证多个领域事件
     * <p>
     * 对事件列表中的每个事件执行验证，如果任何一个事件验证失败，
     * 将抛出ValidationException异常。
     *
     * @param events 要验证的事件列表
     * @throws ValidationException 当任何事件验证失败时抛出异常
     */
    void validateAll(List<T> events);
}
