package ${packageName}.${moduleNameLower}.application.query.handler;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import ${packageName}.${moduleNameLower}.application.query.SayHelloQuery;
import ${packageName}.${moduleNameLower}.application.query.result.ListQueryResult;
import ${packageName}.${moduleNameLower}.application.service.ApplicationService;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import cn.treedeep.king.core.application.cqrs.query.AbstractQueryHandler;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.core.application.cqrs.query.QueryResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class SayHelloQueryHandler extends AbstractQueryHandler<SayHelloQuery, ListQueryResult> {

    @Resource
    private ApplicationService applicationService;

    /**
     * 构造函数
     *
     * @param queryBus 查询总线，用于注册当前命令
     */
    protected SayHelloQueryHandler(QueryBus queryBus) {
        super(queryBus);
    }

    @Override
    protected ListQueryResult doHandle(SayHelloQuery query, CompletableFuture<QueryResult<ListQueryResult>> future) {
        ListQueryResult queryResult = new ListQueryResult();

        List<${entityNameCamel}> list = applicationService.fandAll();
        queryResult.setList(list.stream().map(${entityNameCamel}Dto::new).toList());

        System.out.println("查询doHandle：" + query.getName());

        if (future != null) {
            // 异步调用返回结果
            future.complete(new QueryResult<>(query, queryResult));
        }

        return queryResult;
    }
}
