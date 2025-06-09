package cn.treedeep.king.core.infrastructure.eventstore.archive;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 事件归档实体
 * <p>
 * 用于存储已归档的事件，支持按时间和版本进行归档
 */
@Data
@Entity
@Table(name = "event_archives")
@Comment("事件归档表")
@EntityListeners(AuditingEntityListener.class)
public class ArchivedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Column(name = "aggregate_id")
    @Comment("聚合根ID")
    private String aggregateId;

    @Column(name = "event_type")
    @Comment("事件类型")
    private String eventType;

    @Column(name = "event_data", columnDefinition = "TEXT")
    @Comment("事件数据(JSON格式)")
    private String eventData;

    @Column(name = "aggregate_version")
    @Comment("聚合根版本号")
    private Long aggregateVersion;

    @Column(name = "archived_at")
    @CreatedDate
    @Comment("归档时间")
    private LocalDateTime archivedAt;

    @Column(name = "original_timestamp")
    @Comment("原始事件发生时间")
    private LocalDateTime originalTimestamp;
}
