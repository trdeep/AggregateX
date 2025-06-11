package cn.treedeep.king.core.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 聚合根性能监控切面
 * <p>
 * 基于Micrometer实现的性能监控，自动收集聚合根相关操作的性能指标。
 * <p>
 * 监控维度：
 * <ul>
 * <li>方法执行时间 - 使用Timer测量</li>
 * <li>错误率 - 统计成功/失败比例</li>
 * <li>调用计数 - 统计方法调用次数</li>
 * <li>操作类型 - 区分不同的仓储操作</li>
 * </ul>
 * <p>
 * 监控范围：
 * <ul>
 * <li>Repository接口的所有方法</li>
 * <li>聚合根的关键业务方法</li>
 * <li>命令处理器和事件处理器</li>
 * </ul>
 * <p>
 * 指标标签：
 * <ul>
 * <li>class - 类名</li>
 * <li>method - 方法名</li>
 * <li>status - 执行状态(success/error)</li>
 * <li>exception - 异常类型（仅失败时）</li>
 * </ul>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AggregatePerformanceAspect {

    /**
     * Micrometer指标注册表，用于记录性能指标
     */
    private final MeterRegistry meterRegistry;

    /**
     * 监控仓储方法执行
     * <p>
     * 拦截所有Repository接口的方法调用，记录执行时间和状态
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 如果方法执行过程中发生异常
     */
    @Around("execution(* cn.treedeep.king.core.domain.Repository+.*(..))")
    public Object monitorRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 记录方法执行时间
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            Object result = joinPoint.proceed();

            // 记录成功执行
            sample.stop(Timer.builder("aggregate.repository.execution")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("status", "success")
                    .register(meterRegistry));

            return result;
        } catch (Exception e) {
            // 记录失败执行
            sample.stop(Timer.builder("aggregate.repository.execution")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("status", "error")
                    .tag("error", e.getClass().getSimpleName())
                    .register(meterRegistry));

            throw e;
        }
    }

    /**
     * 监控聚合根方法执行
     * <p>
     * 拦截所有AggregateRoot类的方法调用，记录执行时间和状态
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 如果方法执行过程中发生异常
     */
    @Around("execution(* cn.treedeep.king.core.domain.AggregateRoot+.*(..))")
    public Object monitorAggregateMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 记录方法执行时间
        long startTime = System.nanoTime();

        try {
            Object result = joinPoint.proceed();

            // 记录执行时间
            long duration = System.nanoTime() - startTime;
            meterRegistry.timer("aggregate.method.execution",
                            "class", className,
                            "method", methodName,
                            "status", "success")
                    .record(duration, TimeUnit.NANOSECONDS);

            return result;
        } catch (Exception e) {
            // 记录失败
            long duration = System.nanoTime() - startTime;
            meterRegistry.timer("aggregate.method.execution",
                            "class", className,
                            "method", methodName,
                            "status", "error",
                            "error", e.getClass().getSimpleName())
                    .record(duration, TimeUnit.NANOSECONDS);

            throw e;
        }
    }
}
