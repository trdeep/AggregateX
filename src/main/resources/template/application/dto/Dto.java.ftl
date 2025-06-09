package cn.treedeep.king.${moduleNameLower}.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} DTO
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Getter
@Builder
public class ${moduleNameCamel}Dto {

    private final String id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
