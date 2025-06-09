package cn.treedeep.king.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.${moduleNameLower}.domain.event.${moduleNameCamel}CreatedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} 聚合根
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Entity
@Table(name = "${moduleNameLower}s")
@Comment("${moduleComment}表")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ${moduleNameCamel} extends AggregateRoot<${moduleNameCamel}Id> {

    @EmbeddedId
    @Comment("ID")
    private ${moduleNameCamel}Id id;

    @Column(name = "name", length = 100, nullable = false)
    @Comment("名称")
    private String name;

    @Column(name = "description", length = 500)
    @Comment("描述")
    private String description;

    @Column(name = "created_at", nullable = false)
    @Comment("创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Comment("更新时间")
    private LocalDateTime updatedAt;

    public ${moduleNameCamel}(${moduleNameCamel}Id id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();

        // 发布领域事件
        this.registerEvent(new ${moduleNameCamel}CreatedEvent(this.id, this.name));
    }

    @Override
    public ${moduleNameCamel}Id getId() {
        return this.id;
    }

    public void updateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
}
