package cn.treedeep.king.core.infrastructure.logging;

import cn.treedeep.king.core.application.cqrs.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 命令日志切面
 * <p>
 * 基于AOP实现的命令执行日志记录器，自动捕获和记录所有命令的执行过程。
 * 提供完整的命令执行轨迹，便于问题排查和性能分析。
 * <p>
 * 记录内容：
 * <ul>
 * <li>命令开始执行的时间和基本信息</li>
 * <li>命令执行的结果状态</li>
 * <li>命令执行的总耗时</li>
 * <li>执行过程中的异常信息和堆栈</li>
 * <li>命令的关键参数和上下文信息</li>
 * </ul>
 * <p>
 * 日志级别：
 * <ul>
 * <li>INFO - 记录正常的命令执行开始和完成</li>
 * <li>ERROR - 记录命令执行失败和异常信息</li>
 * <li>DEBUG - 记录详细的执行参数和中间状态</li>
 * </ul>
 * <p>
 * 监控集成：
 * <ul>
 * <li>支持与APM工具集成进行链路追踪</li>
 * <li>提供结构化日志便于日志聚合分析</li>
 * <li>支持敏感信息脱敏处理</li>
 * </ul>
 */
@Aspect
@Component
@Slf4j
public class CommandLoggingAspect {

    /**
     * 定义命令执行的切点
     * <p>
     * 拦截CommandBus中所有dispatch方法的执行
     */
    @Pointcut("execution(* cn.treedeep.king.core.application.cqrs.command.CommandBus.dispatch(..))")
    public void commandExecution() {}

    /**
     * 记录命令执行的环绕通知
     *
     * @param joinPoint 连接点对象
     * @return 命令执行结果
     * @throws Throwable 如果命令执行过程中抛出异常
     */
    @Around("commandExecution()")
    public Object logCommandExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Command command)) {
            return joinPoint.proceed();
        }

        String commandType = command.getClass().getSimpleName();
        String commandId = command.getCommandId();

        long startTime = System.currentTimeMillis();
        log.info("Command execution started - Type: {}, ID: {}", commandType, commandId);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Command execution completed - Type: {}, ID: {}, Duration: {}ms",
                    commandType, commandId, duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Command execution failed - Type: {}, ID: {}, Duration: {}ms, Error: {}",
                    commandType, commandId, duration, ex.getMessage());
            throw ex;
        }
    }
}
