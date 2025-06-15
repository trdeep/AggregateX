package cn.treedeep.king.core.domain;

import cn.treedeep.king.core.domain.validation.DomainValidationException;
import cn.treedeep.king.shared.utils.DateTimeUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 聚合根基类
 * <p>
 * 聚合根是领域驱动设计(DDD)中的核心概念，它是数据修改的边界单元。
 * 所有聚合根实体都必须继承此类，该类提供了以下功能：
 * <ul>
 *   <li>乐观锁控制：通过版本号防止并发修改冲突</li>
 *   <li>软删除支持：标记删除而不是物理删除</li>
 *   <li>审计字段：自动维护创建时间和最后修改时间</li>
 *   <li>领域事件：支持注册和发布领域事件</li>
 *   <li>实体变更跟踪：追踪实体属性的变更</li>
 * </ul>
 *
 * @param <ID> 聚合根唯一标识符类型，必须实现 {@link Identifier} 接口
 * @author AggregateX Framework
 * @since 1.0.0
 */
@MappedSuperclass
@org.jmolecules.ddd.annotation.AggregateRoot
@Getter
public abstract class AggregateRoot<ID extends Identifier> extends AbstractAggregateRoot<AggregateRoot<ID>> {

    /**
     * 版本号，用于乐观锁控制
     * <p>
     * 每次更新聚合根时，版本号会自动递增，防止并发修改导致的数据不一致
     */
    @Version
    @Comment("版本号，用于乐观锁控制")
    private Long version;

    /**
     * 软删除标记
     * <p>
     * 标记该聚合根是否已被删除，默认为 false
     * 被标记为删除的聚合根不能再进行修改操作
     */
    @Comment("是否已删除")
    private boolean deleted;

    /**
     * 最后修改时间
     * <p>
     * 记录聚合根最后一次被修改的时间，每次更新操作都会自动更新此字段
     */
    @Column(name = "last_modified_at")
    @Comment("最后更新时间")
    private OffsetDateTime lastModifiedAt;

    /**
     * 创建时间
     * <p>
     * 记录聚合根被创建的时间，该字段在创建后不可修改
     */
    @Column(name = "created_at", updatable = false)
    @Comment("创建时间")
    private final OffsetDateTime createdAt;

    /**
     * 领域事件列表
     * <p>
     * 存储当前聚合根产生的所有领域事件，这些事件将在事务提交时统一发布
     * 使用 @Transient 注解标记，表示不持久化到数据库
     */
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 实体变更跟踪器
     * <p>
     * 用于跟踪聚合根内部实体的变更，便于进行审计和事件发布
     * 使用 @Transient 注解标记，表示不持久化到数据库
     */
    @Transient
    private final EntityChangeTracker changeTracker = new EntityChangeTracker();

    /**
     * 默认构造函数
     * <p>
     * 初始化创建时间和最后修改时间为当前时间
     */
    protected AggregateRoot() {
        this.createdAt = DateTimeUtil.now();
        this.lastModifiedAt = this.createdAt;
    }

    /**
     * 标记为已删除
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.updateLastModifiedAt();
    }

    /**
     * 更新版本号
     * 如果版本号为空则不更新
     */
    public void updateVersion() {
        if (this.version == null) {
            return;
        }
        this.version += 1;
    }

    /**
     * 更新最后修改时间
     */
    protected void updateLastModifiedAt() {
        this.lastModifiedAt = OffsetDateTime.now();
    }

    /**
     * 检查聚合根是否不可修改
     *
     * @return 如果聚合根已被删除则返回true，否则返回false
     */
    protected boolean immutable() {
        return this.deleted;
    }

    /**
     * 获取聚合根的唯一标识符
     *
     * @return 聚合根的唯一标识符
     */
    public abstract ID getId();

    /**
     * 注册领域事件
     * <p>
     * 一个聚合根可以注册多个事件，在一个最终的操作里会提交处理事件
     *
     * @param event 领域事件
     */
    public void registerEvent(DomainEvent event) {
        if (immutable()) {
            throw new DomainValidationException("DELETED_AGGREGATE", "无法修改已删除的聚合根");
        }
        this.updateLastModifiedAt();
        // super.registerEvent(event);
        domainEvents.add(event);
    }

    /**
     * 获取所有领域事件
     *
     * @return 领域事件列表的副本
     */
    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    /**
     * 清除所有跟踪的变更
     */
    public void clearChanges() {
        changeTracker.clearTracking();
    }

    @Override
    public void clearDomainEvents() {
        // super.clearDomainEvents();
        this.clearChanges();
        this.domainEvents.clear();
    }
}

