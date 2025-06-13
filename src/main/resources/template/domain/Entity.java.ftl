package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.AggregateRoot;
import ${packageName}.${moduleNameLower}.domain.event.${moduleNameCamel}CreatedEvent;
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
 * ${moduleNameCamel}「聚合」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${moduleNameLower}s")
@Comment("${moduleComment}表【聚合】")
@NoArgsConstructor
@Getter
public class ${moduleNameCamel} extends AggregateRoot<${moduleNameCamel}Id> {

    @EmbeddedId
    @Comment("ID")
    private ${moduleNameCamel}Id id;

    @Column(name = "name")
    @Comment("${moduleComment}名称")
    private String name;

    @OneToMany(mappedBy = "${moduleNameLower}", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<${moduleNameCamel}Item> items = new ArrayList<>();


    public ${moduleNameCamel}(${moduleNameCamel}Id id, String name, ${moduleNameCamel}Item... ${moduleNameLower}Items) {
        this.id = id;
        this.name = name;
        this.items.addAll(Arrays.asList(${moduleNameLower}Items));

        for (${moduleNameCamel}Item item : ${moduleNameLower}Items) {
            item.set${moduleNameCamel}(this);
        }

        // 发布领域事件
        this.registerEvent(new ${moduleNameCamel}CreatedEvent(this));
    }

}
