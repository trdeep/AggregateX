package cn.treedeep.king.core.infrastructure.eventstore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.OffsetDateTime;

/**
 * 聚合根快照实体
 * <p>
 * 用于存储聚合根在特定版本时的完整状态快照，主要用于优化事件溯源的性能。
 * 当聚合根的事件数量较多时，通过快照可以避免重放所有历史事件。
 * <p>
 * 快照策略：
 * <ul>
 * <li>定期创建快照 - 每N个事件后创建一次快照</li>
 * <li>重要里程碑快照 - 在关键业务节点创建快照</li>
 * <li>性能优化快照 - 当事件重放时间超过阈值时创建</li>
 * </ul>
 * <p>
 * 使用场景：
 * <ul>
 * <li>聚合根加载：先从快照恢复，再重放后续事件</li>
 * <li>历史状态查询：查看聚合根在特定时间点的状态</li>
 * <li>数据迁移：作为聚合根状态的备份</li>
 * </ul>
 * <p>
 * 数据格式：
 * <ul>
 * <li>快照数据以JSON格式存储，包含聚合根的完整状态</li>
 * <li>版本号标识快照对应的事件版本</li>
 * <li>聚合根类型用于反序列化时的类型识别</li>
 * </ul>
 */
@Entity
@Table(name = "aggregate_snapshots")
@Comment("聚合根快照表")
@Data
public class AggregateSnapshot {

    /**
     * 聚合根的唯一标识符
     */
    @Id
    @Column(name = "aggregate_id")
    @Comment("聚合根ID")
    private String aggregateId;

    /**
     * 聚合根的完整类名
     */
    @Column(name = "aggregate_type")
    @Comment("聚合根类型")
    private String aggregateType;

    /**
     * 快照对应的聚合根版本号
     */
    @Column(name = "version")
    @Comment("快照版本号")
    private Long version;

    /**
     * 聚合根状态的JSON序列化数据
     */
    @Column(name = "snapshot_data", columnDefinition = "TEXT")
    @Comment("快照数据(JSON格式)")
    private String snapshotData;

    /**
     * 快照创建时间
     */
    @Column(name = "created_at")
    @Comment("创建时间")
    private OffsetDateTime createdAt;

}
