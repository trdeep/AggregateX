package cn.treedeep.king.core.domain.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件属性验证注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventPropertyValidation {
    /**
     * 验证失败时的错误消息
     */
    String message() default "";

    /**
     * 属性是否必需
     */
    boolean required() default false;

    /**
     * 字符串最大长度
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * 正则表达式模式
     */
    String pattern() default "";
}
