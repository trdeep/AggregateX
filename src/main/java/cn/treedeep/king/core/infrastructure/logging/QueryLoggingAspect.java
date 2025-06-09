package cn.treedeep.king.core.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 查询日志切面
 * <p>
 * 负责记录查询操作的日志，包括：
 * 1. 查询方法名
 * 2. 查询参数
 * 3. 执行时间
 * 4. 返回结果数量
 * 5. 异常信息
 */
@Aspect
@Component
@Slf4j
public class QueryLoggingAspect {

    @Pointcut("execution(* cn.treedeep.king.core.application.cqrs.query.QueryBus.execute(..))")
    public void queryExecution() {}

    @Around("queryExecution()")
    public Object logQueryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // 记录查询开始
        log.info("Query execution started - Class: {}, Method: {}, Parameters: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 执行查询
            result = joinPoint.proceed();

            // 计算执行时间
            long duration = System.currentTimeMillis() - startTime;

            // 获取结果大小（如果是集合类型）
            int resultSize = getResultSize(result);

            // 记录查询完成
            log.info("Query execution completed - Class: {}, Method: {}, Duration: {}ms, Result size: {}",
                    className, methodName, duration, resultSize);

            return result;
        } catch (Throwable ex) {
            // 记录查询失败
            long duration = System.currentTimeMillis() - startTime;
            log.error("Query execution failed - Class: {}, Method: {}, Duration: {}ms, Error: {}",
                    className, methodName, duration, ex.getMessage());
            throw ex;
        }
    }

    /**
     * 获取查询结果的大小
     * 如果结果是集合类型，返回集合大小
     * 如果是分页结果，返回总记录数
     * 否则返回1（单个对象）或0（空结果）
     */
    private int getResultSize(Object result) {
        return switch (result) {
            case null -> 0;
            case java.util.Collection<?> collection -> collection.size();
            case org.springframework.data.domain.Page<?> page -> (int) page.getTotalElements();
            default -> 1;
        };

    }
}
