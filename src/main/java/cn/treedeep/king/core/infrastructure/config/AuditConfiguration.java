package cn.treedeep.king.core.infrastructure.config;

import cn.treedeep.king.core.infrastructure.audit.AggregateAuditAspect;
import cn.treedeep.king.core.infrastructure.audit.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 审计功能配置类
 * <p>
 * 只有在JPA可用且启用审计功能时才加载
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.springframework.data.jpa.repository.JpaRepository")
@ConditionalOnProperty(name = "app.audit.enabled", havingValue = "true", matchIfMissing = false)
public class AuditConfiguration {

    /**
     * 聚合根审计切面
     * 只有在JPA和AuditLogRepository都可用时才启用
     */
    @Bean
    public AggregateAuditAspect aggregateAuditAspect(AuditLogRepository auditLogRepository) {
        log.info("Enabling aggregate audit aspect with JPA repository");
        return new AggregateAuditAspect(auditLogRepository);
    }
}
