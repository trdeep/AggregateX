package cn.treedeep.king.${moduleNameLower}.domain;

import org.jmolecules.ddd.types.Identifier;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel}唯一标识符
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ${moduleNameCamel}Id implements Identifier {

    @Column(name = "${moduleNameLower}_id", length = 36, nullable = false)
    @Comment("ID")
    private String value;

    public ${moduleNameCamel}Id(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("${moduleNameCamel} ID cannot be null or empty");
        }
        this.value = value;
    }

    public static ${moduleNameCamel}Id generate() {
        return new ${moduleNameCamel}Id(UUID.randomUUID().toString());
    }

    public static ${moduleNameCamel}Id of(String id) {
        return new ${moduleNameCamel}Id(id);
    }

    @Override
    public String toString() {
        return value;
    }
}
