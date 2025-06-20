package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.BaseIdentifier;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel} 「聚合根标识」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Embeddable
@EqualsAndHashCode(callSuper = true)
@Getter
public class ${entityNameCamel}Id extends BaseIdentifier {

    public ${entityNameCamel}Id() {
        super();
    }

    public ${entityNameCamel}Id(String value) {
        super(value);

        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("${entityNameCamel} ID cannot be null or empty");
        }
    }

    public static ${entityNameCamel}Id generate() {
        return new ${entityNameCamel}Id(UUID.randomUUID().toString());
    }

    public static ${entityNameCamel}Id of(String id) {
        return new ${entityNameCamel}Id(id);
    }
}
