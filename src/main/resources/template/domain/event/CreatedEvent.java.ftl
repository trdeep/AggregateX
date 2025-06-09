package cn.treedeep.king.${moduleNameLower}.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} 创建事件
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Entity
@Table(name = "${moduleNameLower}_created_events")
@Comment("创建商品事件表")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ${moduleNameCamel}CreatedEvent extends DomainEvent {

    @Column(name = "${moduleNameLower}_id", length = 36)
    @Comment("ID")
    private  ${moduleNameCamel}Id ${moduleNameLower}Id;

    @Column(name = "name", length = 36)
    @Comment("名称")
    private  String name;

    public ${moduleNameCamel}CreatedEvent(${moduleNameCamel}Id ${moduleNameLower}Id, String name) {
        super();
        this.${moduleNameLower}Id = ${moduleNameLower}Id;
        this.name = name;
    }
}
