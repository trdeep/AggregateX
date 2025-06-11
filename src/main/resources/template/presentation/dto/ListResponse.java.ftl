package cn.treedeep.king.${moduleNameLower}.presentation.dto;

import cn.treedeep.king.${moduleNameLower}.application.dto.${moduleNameCamel}Dto;
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
public class ${moduleNameCamel}ListResponse {

    @Schema(description = "${moduleComment}列表")
    private final List<${moduleNameCamel}Dto> ${moduleNameLower}s;

    @Schema(description = "总数量")
    public int getTotal() {
        return ${moduleNameLower}s != null ? ${moduleNameLower}s.size() : 0;
    }
}
