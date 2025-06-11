package cn.treedeep.king.${moduleNameLower}.application.query;

import cn.treedeep.king.${moduleNameLower}.application.dto.${moduleNameCamel}Dto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表查询结果
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RequiredArgsConstructor
@Getter
public class ${moduleNameCamel}ListQueryResult {

    private final List<${moduleNameCamel}Dto> ${moduleNameLower}s;
    private final int total;
}
