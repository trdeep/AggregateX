package cn.treedeep.king.${moduleNameLower}.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} DTO
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Getter
@Builder
public class ${moduleNameCamel}Dto {

    private final String id;
    private final String name;
    private final List<${moduleNameCamel}ItemDto> items;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
