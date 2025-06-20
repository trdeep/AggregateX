package cn.treedeep.king.core.application.cqrs.query;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 查询总线
 * <p>
 * 负责分发查询到对应的处理器
 */
@Service
public class QueryBus {
    private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> handlers = new HashMap<>();

    /**
     * 注册查询处理器
     *
     * @param handler 查询处理器
     */
    public void register(QueryHandler<?, ?> handler) {
        handlers.put(handler.getQueryType(), handler);
    }

    /**
     * 执行查询
     *
     * @param query 查询对象
     * @param <R>   查询结果类型
     * @return 查询结果
     */
    public <R> R execute(Query<R> query) {
        return execute(query, null);
    }

    @SuppressWarnings("unchecked")
    public <R> R execute(Query<R> query, CompletableFuture<QueryResult<R>> future) {

        Class<? extends Query<?>> queryType = (Class<? extends Query<?>>) query.getClass();
        QueryHandler<Query<R>, R> handler = (QueryHandler<Query<R>, R>) handlers.get(queryType);

        if (handler == null) {
            IllegalStateException ex = new IllegalStateException("No handler registered for query type: " + queryType.getName());
            future.completeExceptionally(ex);
            throw ex;
        }

        return handler.handle(query, future);
    }
}
