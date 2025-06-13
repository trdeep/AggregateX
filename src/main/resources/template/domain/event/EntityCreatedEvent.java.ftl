package ${packageName}.${moduleNameLower}.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import cn.treedeep.king.shared.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
* Copyright © ${copyright} 版权所有
* <p>
    * ${moduleComment}创建「领域事件」
    * <p>
    * Power by AggregateX
    *
    * @author ${author}
    * @since ${dateTime}
    */
    @Entity
    @Table(name = "${entityNameLower}_created_events")
    @Comment("创建${moduleComment}事件表【事件】")
    @NoArgsConstructor
    @Getter
    public class ${entityNameCamel}CreatedEvent extends DomainEvent {

    @Column(name = "name", length = 36)
    @Comment("名称")
    private  String name;

    public ${entityNameCamel}CreatedEvent(${entityNameCamel} aggregate) {
    super();
    this.name = aggregate.getName();
    setAggregateId(aggregate.getId().getValue());
    setAggregateVersion(aggregate.getVersion());
    setData(JsonUtil.toJson(aggregate)); // 实际数据自定义
    }
    }
