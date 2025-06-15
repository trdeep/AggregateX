package cn.treedeep.king.core.domain;

import cn.treedeep.king.shared.utils.DateTimeUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.context.ApplicationEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 领域事件基类
 * <p>
 * 所有的领域事件都需要继承此类
 */
@Entity
@Table(name = "domain_events")
@Comment("领域事件表 - 请勿直接使用，而是继承自定义事件")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class DomainEvent extends ApplicationEvent {

    @Id
    @Comment("事件ID")
    @Column(name = "event_id")
    private final String eventId;

    @Comment("数据")
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    @Comment("事件发生时间")
    @Column(name = "occurred_on")
    private final OffsetDateTime occurredOn;

    @Comment("聚合根ID")
    @Column(name = "aggregate_id")
    private String aggregateId;

    @Comment("聚合根版本号")
    @Column(name = "aggregate_version")
    private Long aggregateVersion;


    protected DomainEvent() {
        super("DomainEvent");
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = DateTimeUtil.now();
    }

    protected DomainEvent(Object source) {
        super(source);
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = DateTimeUtil.now();
    }

}

