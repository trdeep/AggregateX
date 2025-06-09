package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义验证规则注册器
 */
@Component
public class CustomValidationRegistry {
    private final Map<Class<? extends DomainEvent>, List<DomainEventValidator<?>>> validators = new HashMap<>();

    /**
     * 注册自定义验证规则
     *
     * @param eventType 事件类型
     * @param validator 自定义验证器
     */
    public <T extends DomainEvent> void registerValidator(Class<T> eventType,
                                                        DomainEventValidator<T> validator) {
        validators.computeIfAbsent(eventType, k -> new ArrayList<>())
                 .add(validator);
    }

    /**
     * 执行所有适用的验证规则
     *
     * @param event 要验证的事件
     */
    @SuppressWarnings("unchecked")
    public void validate(DomainEvent event) {
        List<DomainEventValidator<?>> eventValidators = validators.get(event.getClass());
        if (eventValidators != null) {
            for (DomainEventValidator validator : eventValidators) {
                validator.validate(event);
            }
        }
    }

    /**
     * 移除特定事件类型的验证器
     *
     * @param eventType 事件类型
     * @param validator 要移除的验证器
     */
    public <T extends DomainEvent> void removeValidator(Class<T> eventType,
                                                      DomainEventValidator<T> validator) {
        List<DomainEventValidator<?>> eventValidators = validators.get(eventType);
        if (eventValidators != null) {
            eventValidators.remove(validator);
        }
    }

    /**
     * 清除所有验证规则
     */
    public void clearAllValidators() {
        validators.clear();
    }
}
