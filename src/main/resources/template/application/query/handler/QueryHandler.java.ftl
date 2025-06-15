package ${packageName}.${moduleNameLower}.application.query.handler;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import ${packageName}.${moduleNameLower}.application.query.SayHelloQuery;
import ${packageName}.${moduleNameLower}.application.query.result.ListQueryResult;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.repository.${entityNameCamel}AggregateRepository;
import cn.treedeep.king.core.application.cqrs.query.AbstractQueryHandler;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.core.application.cqrs.query.QueryResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class SayHelloQueryHandler extends AbstractQueryHandler<SayHelloQuery, ${entityNameCamel}AggregateRepository, ListQueryResult> {

    protected SayHelloQueryHandler(${entityNameCamel}AggregateRepository repository, QueryBus queryBus) {
        super(repository, queryBus);
    }

    @Override
    protected ListQueryResult doHandle(SayHelloQuery query, CompletableFuture<QueryResult<ListQueryResult>> future) {
        ListQueryResult queryResult = new ListQueryResult();

        List<${entityNameCamel}> list = aggregateRepository.findAll();
        queryResult.setList(list.stream().map(${entityNameCamel}Dto::new).toList());

        System.out.println("查询doHandle：" + query.getName());

        if (future != null) {
            // 异步调用返回结果
            future.complete(new QueryResult<>(query, queryResult));
        }

        return queryResult;
    }
}
