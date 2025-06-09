package cn.treedeep.king.core.infrastructure.audit;

import cn.treedeep.king.core.domain.AggregateRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

/**
 * 聚合根操作审计切面
 * <p>
 * 自动记录聚合根的所有修改操作,包括:<br>
 * 1. 操作类型(创建/更新/删除)<br>
 * 2. 操作时间<br>
 * 3. 操作人<br>
 * 4. 变更内容
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class AggregateAuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Around("execution(* cn.treedeep.king.core.domain.AbstractRepository+.save(..))")
    public Object auditSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof AggregateRoot<?> aggregate) {
            // 记录保存前的状态
            String aggregateType = aggregate.getClass().getSimpleName();
            String aggregateId = aggregate.getId().toString();
            String operation = aggregate.getVersion() == null ? "CREATE" : "UPDATE";

            // 执行实际的保存操作
            Object result = joinPoint.proceed();

            // 创建审计日志
            AuditLog auditLog = new AuditLog();
            auditLog.setAggregateType(aggregateType);
            auditLog.setAggregateId(aggregateId);
            auditLog.setOperation(operation);
            auditLog.setOperationTime(LocalDateTime.now());
            auditLog.setTransactionId(getCurrentTransactionId());

            // 保存审计日志
            auditLogRepository.save(auditLog);

            return result;
        }
        return joinPoint.proceed();
    }

    @Around("execution(* cn.treedeep.king.core.domain.Repository+.delete(..))")
    public Object auditDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof AggregateRoot<?> aggregate) {
            // 记录删除信息
            AuditLog auditLog = new AuditLog();
            auditLog.setAggregateType(aggregate.getClass().getSimpleName());
            auditLog.setAggregateId(aggregate.getId().toString());
            auditLog.setOperation("DELETE");
            auditLog.setOperationTime(LocalDateTime.now());
            auditLog.setTransactionId(getCurrentTransactionId());

            // 执行实际的删除操作
            Object result = joinPoint.proceed();

            // 保存审计日志
            auditLogRepository.save(auditLog);

            return result;
        }
        return joinPoint.proceed();
    }

    private String getCurrentTransactionId() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
