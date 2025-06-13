package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.AggregateRoot;
import ${packageName}.${moduleNameLower}.domain.event.${entityNameCamel}CreatedEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「聚合」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${entityNameLower}s")
@Comment("${moduleComment}表【聚合】")
@NoArgsConstructor
@Getter
public class ${entityNameCamel} extends AggregateRoot<${entityNameCamel}Id> {

    @EmbeddedId
    @Comment("ID")
    private ${entityNameCamel}Id id;

    @Column(name = "name")
    @Comment("${moduleComment}名称")
    private String name;

    @OneToMany(mappedBy = "${entityNameLower}", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<${entityNameCamel}Item> items = new ArrayList<>();


    public ${entityNameCamel}(${entityNameCamel}Id id, String name, ${entityNameCamel}Item... ${entityNameLower}Items) {
        this.id = id;
        this.name = name;
        this.items.addAll(Arrays.asList(${entityNameLower}Items));

        for (${entityNameCamel}Item item : ${entityNameLower}Items) {
            item.set${entityNameCamel}(this);
        }

        // 发布领域事件
        this.registerEvent(new ${entityNameCamel}CreatedEvent(this));
    }

}
