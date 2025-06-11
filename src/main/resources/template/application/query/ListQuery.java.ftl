package cn.treedeep.king.${moduleNameLower}.application.query;

import cn.treedeep.king.core.application.cqrs.query.Query;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表「查询」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class ${moduleNameCamel}ListQuery extends Query<${moduleNameCamel}ListQueryResult> {

    private final int page;
    private final int size;
    private final String name;

    @Override
    public String getQueryName() {
        return "${moduleNameCamel}ListQuery";
    }
}
