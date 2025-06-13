package ${packageName}.${moduleNameLower}.presentation.dto;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表「响应」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RequiredArgsConstructor
@Getter
@Schema(description = "${moduleComment}列表响应")
public class ${entityNameCamel}ListResponse {

    @Schema(description = "${moduleComment}列表")
    private final List<${entityNameCamel}Dto> ${entityNameLower}s;

    @Schema(description = "总数量")
    public int getTotal() {
        return ${entityNameLower}s != null ? ${entityNameLower}s.size() : 0;
    }
}
