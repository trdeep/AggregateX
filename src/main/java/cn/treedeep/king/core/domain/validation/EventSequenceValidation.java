package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件序列验证注解
 * <p>
 * 用于声明事件顺序约束
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSequenceValidation {
    /**
     * 必须在此事件之前发生的事件类型
     */
    Class<? extends DomainEvent>[] requiredPrecedingEvents() default {};

    /**
     * 不能在此事件之后发生的事件类型
     */
    Class<? extends DomainEvent>[] forbiddenFollowingEvents() default {};
}
