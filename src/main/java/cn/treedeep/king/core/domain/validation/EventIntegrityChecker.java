package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.ValidationException;

import java.util.List;

/**
 * 事件完整性检查器接口
 */
public interface EventIntegrityChecker {
    /**
     * 检查事件完整性
     * - 检查必填字段
     * - 验证事件版本连续性
     * - 验证事件时间顺序
     * - 检查事件引用完整性
     *
     * @param events 要检查的事件列表
     * @throws ValidationException 当完整性检查失败时
     */
    void checkIntegrity(List<DomainEvent> events);
}
