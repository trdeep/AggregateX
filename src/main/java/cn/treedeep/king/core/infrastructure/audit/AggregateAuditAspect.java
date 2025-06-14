package cn.treedeep.king.core.infrastructure.audit;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.shared.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    /**
     * 审计保存操作
     * <p>
     * 拦截所有Repository的save方法，记录聚合根的创建和更新操作
     *
     * @param joinPoint 连接点
     * @return 保存操作的结果
     * @throws Throwable 如果操作执行过程中发生异常
     */
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
            auditLog.setOperationTime(DateTimeUtil.now());
            auditLog.setTransactionId(getCurrentTransactionId());

            // 保存审计日志
            auditLogRepository.save(auditLog);

            return result;
        }
        return joinPoint.proceed();
    }

    /**
     * 审计删除操作
     * <p>
     * 拦截所有Repository的delete方法，记录聚合根的删除操作
     *
     * @param joinPoint 连接点
     * @return 删除操作的结果
     * @throws Throwable 如果操作执行过程中发生异常
     */
    @Around("execution(* cn.treedeep.king.core.domain.AbstractRepository+.delete(..))")
    public Object auditDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof AggregateRoot<?> aggregate) {
            // 记录删除信息
            AuditLog auditLog = new AuditLog();
            auditLog.setAggregateType(aggregate.getClass().getSimpleName());
            auditLog.setAggregateId(aggregate.getId().toString());
            auditLog.setOperation("DELETE");
            auditLog.setOperationTime(DateTimeUtil.now());
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
