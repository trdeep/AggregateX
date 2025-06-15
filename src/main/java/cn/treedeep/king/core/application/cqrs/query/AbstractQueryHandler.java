package cn.treedeep.king.core.application.cqrs.query;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象查询处理器
 * <p>
 * 提供查询处理的基础设施，包括：<br>
 * 1. 事务支持(默认只读)<br>
 * 2. 性能监控<br>
 * 3. 异常处理<br>
 * 4. 日志记录
 *
 * @param <Q> 查询类型
 * @param <R> 结果类型
 */

@Slf4j
@Getter
public abstract class AbstractQueryHandler<Q extends Query<R>, R> implements QueryHandler<Q, R> {
    private final Class<Q> queryType;
    private final QueryBus queryBus;

    /**
     * 构造函数
     *
     * @param queryBus 查询总线，用于注册当前命令
     */
    @SuppressWarnings("unchecked")
    protected AbstractQueryHandler(QueryBus queryBus) {
        this.queryBus = queryBus;
        this.queryType = (Class<Q>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(getClass(), AbstractQueryHandler.class))[0];

        // 在构造时自动注册到查询总线
        queryBus.register(this);
    }

    @Override
    @Transactional(readOnly = true)
    public R handle(Q query, CompletableFuture<QueryResult<R>> future) {
        String queryName = query.getQueryName();
        try {
            log.debug("开始处理查询: {}", queryName);

            long startTime = System.currentTimeMillis();

            // 1. 验证查询参数
            validateQuery(query);

            // 2. 执行具体查询逻辑
            R result = doHandle(query, future);

            // 3. 记录查询耗时
            long endTime = System.currentTimeMillis();

            log.debug("查询处理完成: {}, 耗时: {}ms", queryName, endTime - startTime);
            return result;
        } catch (Exception e) {
            log.error("查询处理失败: {}, 错误: {}", queryName, e.getMessage());
            throw new QueryException("查询处理失败: " + queryName, e);
        }
    }

    /**
     * 验证查询参数
     *
     * @param query 要验证的查询对象
     */
    protected void validateQuery(Q query) {
        if (query == null) {
            throw new IllegalArgumentException("查询对象不能为空");
        }
    }

    /**
     * 执行具体的查询逻辑
     * <p>
     * 子类必须实现此方法来处理特定的查询请求
     *
     * @param query 查询对象
     * @return 查询结果
     */
    protected abstract R doHandle(Q query, CompletableFuture<QueryResult<R>> future);

}
