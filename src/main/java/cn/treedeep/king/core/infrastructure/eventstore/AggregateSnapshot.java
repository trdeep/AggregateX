package cn.treedeep.king.core.infrastructure.eventstore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 聚合根快照
 * <p>
 * 用于存储聚合根的状态快照，提高事件溯源时的性能
 */
@Entity
@Table(name = "aggregate_snapshots")
@Comment("聚合根快照表")
@Data
public class AggregateSnapshot {

    @Id
    @Column(name = "aggregate_id")
    @Comment("聚合根ID")
    private String aggregateId;

    @Column(name = "aggregate_type")
    @Comment("聚合根类型")
    private String aggregateType;

    @Column(name = "version")
    @Comment("快照版本号")
    private Long version;

    @Column(name = "snapshot_data", columnDefinition = "TEXT")
    @Comment("快照数据(JSON格式)")
    private String snapshotData;

    @Column(name = "created_at")
    @Comment("创建时间")
    private LocalDateTime createdAt;

}
