package cn.treedeep.king.${moduleNameLower}.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel}Item「DTO」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Getter
@Builder
public class ${moduleNameCamel}ItemDto {

    private String id;
    private String ${moduleNameLower}Id;
    private String name;
    private String description;
    private Long createdAt;
    private LocalDateTime updatedAt;
}
