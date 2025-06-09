package cn.treedeep.king.config;

import cn.treedeep.king.core.infrastructure.config.AggregateXAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用AggregateX DDD框架
 * <p>
 * 在Spring Boot应用的主类或配置类上添加此注解即可启用AggregateX框架
 * <p>
 * 使用示例：
 * <pre>
 * {@code
 * @SpringBootApplication
 * @EnableAggregateX
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AggregateXAutoConfiguration.class)
public @interface EnableAggregateX {
}
