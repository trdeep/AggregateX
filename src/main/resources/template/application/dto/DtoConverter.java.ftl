package ${packageName}.${moduleNameLower}.application.dto;

import cn.treedeep.king.shared.utils.DateTimeUtil;
import ${packageName}.${moduleNameLower}.domain.${moduleNameCamel};
import ${packageName}.${moduleNameLower}.domain.${moduleNameCamel}Item;
import org.springframework.stereotype.Component;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel}「DTO 转换器」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
public class ${moduleNameCamel}DtoConverter {

    public ${moduleNameCamel}Dto toDto(${moduleNameCamel} ${moduleNameLower}) {
        return ${moduleNameCamel}Dto.builder()
                .id(${moduleNameLower}.getId().getValue())
                .name(${moduleNameLower}.getName())
                .items(${moduleNameLower}.getItems().stream().map(this::toDto).toList())
                .createdAt(DateTimeUtil.format(${moduleNameLower}.getCreatedAt()))
                .updatedAt(${moduleNameLower}.getLastModifiedAt())
                .build();
    }

    public ${moduleNameCamel}ItemDto toDto(${moduleNameCamel}Item item) {
        return ${moduleNameCamel}ItemDto.builder()
                .id(item.getId().getValue())
                .name(item.getName())
                .${moduleNameLower}Id(item.get${moduleNameCamel}().getId().getValue())
                .description(item.getDescription().getValue())
                .createdAt(DateTimeUtil.toTimestamp(item.getCreatedAt()))
                .updatedAt(DateTimeUtil.toLocalDateTime(item.getUpdatedAt()))
                .build();
    }
}
