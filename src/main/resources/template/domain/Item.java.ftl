package cn.treedeep.king.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.EntityBase;
import cn.treedeep.king.core.domain.UIdentifier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.OffsetDateTime;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel}Item「实体」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${moduleNameLower}_items")
@Comment("${moduleComment}子项表【实体】")
@NoArgsConstructor
@Getter
public class ${moduleNameCamel}Item extends EntityBase<UIdentifier> {

    public ${moduleNameCamel}Item(String name, String description) {
        this.id = UIdentifier.generate();
        this.name = name;
        this.description = new Description(id.getValue(), description);
        this.createdAt = OffsetDateTime.now();
    }

    @EmbeddedId
    @Comment("ID")
    private UIdentifier id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "${moduleNameLower}_id")
    @Comment("${moduleComment}ID")
    private ${moduleNameCamel} ${moduleNameLower};

    @Column(name = "name", length = 100, nullable = false)
    @Comment("名称")
    private String name;

    @Setter
    @Column(name = "description",columnDefinition = "TEXT")
    @Convert(converter = Description.DescriptionJsonConverter.class)
    @Comment("描述")
    private Description description;

    @Column(name = "created_at", nullable = false)
    @Comment("创建时间")
    private OffsetDateTime createdAt;

    @Setter
    @Column(name = "updated_at")
    @Comment("更新时间")
    private OffsetDateTime updatedAt;


    public void updateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
        this.updatedAt = OffsetDateTime.now();
    }


    public static ${moduleNameCamel}Item create(String name, String description) {
        return new ${moduleNameCamel}Item(name, description);
    }

}
