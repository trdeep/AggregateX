package ${packageName}.${moduleNameLower}.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「DTO」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Getter
@Builder
public class ${entityNameCamel}Dto {

    private final String id;
    private final String name;
    private final List<${entityNameCamel}ItemDto> items;
    private final String createdAt;
    private final OffsetDateTime updatedAt;
}
