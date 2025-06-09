package cn.treedeep.king.core.domain;

import cn.treedeep.king.core.domain.validation.CustomValidationRegistry;
import cn.treedeep.king.core.domain.validation.DomainEventValidator;
import cn.treedeep.king.core.domain.validation.EventIntegrityChecker;
import cn.treedeep.king.core.domain.validation.BusinessConstraintValidator;
import cn.treedeep.king.core.domain.validation.CustomValidationRegistry;
import cn.treedeep.king.core.domain.validation.DomainEventValidator;
import cn.treedeep.king.core.domain.validation.EventIntegrityChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 抽象事件处理器基类
 * <p>
 * 提供事件处理的基础设施，包括：<br>
 * 1. 事务支持<br>
 * 2. 异常处理<br>
 * 3. 日志记录<br>
 * 4. 事件验证
 *
 * @param <T> 要处理的领域事件类型
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T extends DomainEvent> implements EventHandler<T> {

    private final DomainEventValidator<T> validator;
    private final BusinessConstraintValidator businessValidator;
    private final EventIntegrityChecker integrityChecker;
    private final CustomValidationRegistry customValidationRegistry;

    @Override
    @Transactional
    public void handle(T event) {
        try {
            log.debug("开始处理事件: {}, 聚合ID: {}, 版本: {}",
                    event.getClass().getSimpleName(),
                    event.getAggregateId(),
                    event.getAggregateVersion());

            // 1. 执行基础验证
            validator.validate(event);

            // 2. 执行完整性检查
            integrityChecker.checkIntegrity(List.of(event));

            // 3. 执行自定义验证规则
            customValidationRegistry.validate(event);

            // 4. 处理事件
            doHandle(event);

            log.debug("事件处理完成: {}, 聚合ID: {}",
                    event.getClass().getSimpleName(),
                    event.getAggregateId());
        } catch (Exception e) {
            log.error("事件处理失败: {}, 聚合ID: {}, 错误: {}",
                    event.getClass().getSimpleName(),
                    event.getAggregateId(),
                    e.getMessage());
            throw e;
        }
    }

    /**
     * 具体的事件处理逻辑
     *
     * @param event 要处理的领域事件
     */
    protected abstract void doHandle(T event);
}
