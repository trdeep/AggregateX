package ${packageName}.${moduleNameLower}.application.dto;

import cn.treedeep.king.shared.utils.DateTimeUtil;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Item;
import org.springframework.stereotype.Component;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「DTO 转换器」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
public class ${entityNameCamel}DtoConverter {

    public ${entityNameCamel}Dto toDto(${entityNameCamel} ${entityNameLower}) {
        return ${entityNameCamel}Dto.builder()
                .id(${entityNameLower}.getId().getValue())
                .name(${entityNameLower}.getName())
                .items(${entityNameLower}.getItems().stream().map(this::toDto).toList())
                .createdAt(DateTimeUtil.format(${entityNameLower}.getCreatedAt()))
                .updatedAt(${entityNameLower}.getLastModifiedAt())
                .build();
    }

    public ${entityNameCamel}ItemDto toDto(${entityNameCamel}Item item) {
        return ${entityNameCamel}ItemDto.builder()
                .id(item.getId().getValue())
                .name(item.getName())
                .${entityNameLower}Id(item.get${entityNameCamel}().getId().getValue())
                .description(item.getDescription().getValue())
                .createdAt(DateTimeUtil.toTimestamp(item.getCreatedAt()))
                .updatedAt(DateTimeUtil.toLocalDateTime(item.getUpdatedAt()))
                .build();
    }
}
