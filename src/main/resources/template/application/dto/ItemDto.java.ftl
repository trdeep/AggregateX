package ${packageName}.${moduleNameLower}.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}Item「DTO」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Getter
@Builder
public class ${entityNameCamel}ItemDto {

    private String id;
    private String ${entityNameLower}Id;
    private String name;
    private String description;
    private Long createdAt;
    private LocalDateTime updatedAt;
}
