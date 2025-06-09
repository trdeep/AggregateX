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
 * 监控:<br>
 * 1. 方法执行时间<br>
 * 2. 错误率<br>
 * 3. 调用计数
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AggregatePerformanceAspect {

    private final MeterRegistry meterRegistry;

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
