package ${packageName}.${moduleNameLower}.application.query;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表查询「结果」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RequiredArgsConstructor
@Getter
public class ${entityNameCamel}ListQueryResult {

    private final List<${entityNameCamel}Dto> ${entityNameLower}s;
    private final int total;
}
