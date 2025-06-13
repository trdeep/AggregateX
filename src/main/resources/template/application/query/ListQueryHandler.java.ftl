package ${packageName}.${moduleNameLower}.application.query;

import cn.treedeep.king.core.application.cqrs.query.AbstractQueryHandler;
import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import ${packageName}.${moduleNameLower}.application.service.${entityNameCamel}ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}列表查询「处理器」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ${entityNameCamel}ListQueryHandler extends AbstractQueryHandler<${entityNameCamel}ListQuery, ${entityNameCamel}ListQueryResult> {

    private final ${entityNameCamel}ApplicationService ${entityNameLower}ApplicationService;

    @Override
    protected ${entityNameCamel}ListQueryResult doHandle(${entityNameCamel}ListQuery query) {
        log.info("Processing ${entityNameCamel}ListQuery");

        List<${entityNameCamel}Dto> collect = ${entityNameLower}ApplicationService.findAll();

        return new ${entityNameCamel}ListQueryResult(collect, collect.size());
    }
}
