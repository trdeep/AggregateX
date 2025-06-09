package cn.treedeep.king.${moduleNameLower}.application.dto;

import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import org.springframework.stereotype.Component;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} DTO 转换器
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Component
public class ${moduleNameCamel}DtoConverter {

    public ${moduleNameCamel}Dto toDto(${moduleNameCamel} ${moduleNameLower}) {
        return ${moduleNameCamel}Dto.builder()
                .id(${moduleNameLower}.getId().toString())
                .name(${moduleNameLower}.getName())
                .description(${moduleNameLower}.getDescription())
                .createdAt(${moduleNameLower}.getCreatedAt())
                .updatedAt(${moduleNameLower}.getUpdatedAt())
                .build();
    }
}
