package cn.treedeep.king.core.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 聚合根ID验证注解
 */
@Documented
@Constraint(validatedBy = AggregateIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAggregateId {
    String message() default "Invalid aggregate id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
