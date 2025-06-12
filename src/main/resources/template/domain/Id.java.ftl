package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.BaseIdentifier;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} ID「值对象」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Embeddable
@EqualsAndHashCode(callSuper = true)
@Getter
public class ${moduleNameCamel}Id extends BaseIdentifier {

    public ${moduleNameCamel}Id() {
        super();
    }

    public ${moduleNameCamel}Id(String value) {
        super(value);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("${moduleNameCamel} ID cannot be null or empty");
        }
    }

    public static ${moduleNameCamel}Id generate() {
        return new ${moduleNameCamel}Id(UUID.randomUUID().toString());
    }

    public static ${moduleNameCamel}Id of(String id) {
        return new ${moduleNameCamel}Id(id);
    }
}
