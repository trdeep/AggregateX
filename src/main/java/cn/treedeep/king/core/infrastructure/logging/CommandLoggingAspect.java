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
 * 负责记录命令的执行日志，包括：
 * 1. 命令开始执行的时间
 * 2. 命令执行的结果
 * 3. 命令执行的耗时
 * 4. 执行过程中的异常信息
 */
@Aspect
@Component
@Slf4j
public class CommandLoggingAspect {

    @Pointcut("execution(* cn.treedeep.king.core.application.cqrs.command.CommandBus.dispatch(..))")
    public void commandExecution() {}

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
