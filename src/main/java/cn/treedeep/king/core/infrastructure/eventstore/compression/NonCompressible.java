package cn.treedeep.king.core.infrastructure.eventstore.compression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记不可压缩的事件
 * <p>
 * 用于标记那些不应该被压缩的重要业务事件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonCompressible {
    /**
     * 可选的说明，解释为什么这个事件不能被压缩
     */
    String value() default "";
}
