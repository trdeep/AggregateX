package cn.treedeep.king.core.infrastructure.audit;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.OffsetDateTime;

/**
 * 审计日志实体
 */
@Entity
@Table(name = "audit_logs")
@Comment("审计日志表")

@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Comment("聚合根类型")
    private String aggregateType;

    @Comment("聚合根ID")
    private String aggregateId;

    @Comment("操作类型")
    private String operation;

    @Comment("操作时间")
    private OffsetDateTime operationTime;

    @Comment("事务ID")
    private String transactionId;

    @Comment("操作人ID")
    private String operatorId;

    @Comment("变更详情(JSON)")
    @Column(columnDefinition = "TEXT")
    private String changeDetails;
}
