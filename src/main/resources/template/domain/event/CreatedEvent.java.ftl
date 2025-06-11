package cn.treedeep.king.${moduleNameLower}.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.shared.utils.JsonUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}创建「事件」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${moduleNameLower}_created_events")
@Comment("【创建${moduleComment}】事件表")
@NoArgsConstructor
@Getter
public class ${moduleNameCamel}CreatedEvent extends DomainEvent {

    @Column(name = "name", length = 36)
    @Comment("名称")
    private  String name;

    public ${moduleNameCamel}CreatedEvent(${moduleNameCamel} aggregate) {
        super();
        this.name = aggregate.getName();
        setAggregateId(aggregate.getId().getValue());
        setAggregateVersion(aggregate.getVersion());
        setData(JsonUtils.toJson(this));
    }
}
