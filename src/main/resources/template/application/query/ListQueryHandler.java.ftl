package cn.treedeep.king.${moduleNameLower}.application.query;

import cn.treedeep.king.core.application.cqrs.query.AbstractQueryHandler;
import cn.treedeep.king.${moduleNameLower}.application.service.${moduleNameCamel}ApplicationService;
import cn.treedeep.king.${moduleNameLower}.application.dto.${moduleNameCamel}Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表查询处理器
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ${moduleNameCamel}ListQueryHandler extends AbstractQueryHandler<${moduleNameCamel}ListQuery, ${moduleNameCamel}ListQueryResult> {

    private final ${moduleNameCamel}ApplicationService ${moduleNameLower}ApplicationService;

    @Override
    protected ${moduleNameCamel}ListQueryResult doHandle(${moduleNameCamel}ListQuery query) {
        log.info("Processing ${moduleNameCamel}ListQuery");

        List<${moduleNameCamel}Dto> collect = ${moduleNameLower}ApplicationService.findAll();

        return new ${moduleNameCamel}ListQueryResult(collect, collect.size());
    }
}
