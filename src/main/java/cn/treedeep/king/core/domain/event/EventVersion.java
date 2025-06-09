package cn.treedeep.king.core.domain.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件版本注解
 * <p>
 * 用于标记领域事件的版本信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventVersion {
    /**
     * 事件版本号
     *
     * @return 版本号
     */
    int value();

    /**
     * 事件版本描述
     *
     * @return 版本变更说明
     */
    String description() default "";
}
