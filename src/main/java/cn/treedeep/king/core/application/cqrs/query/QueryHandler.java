package cn.treedeep.king.core.application.cqrs.query;

/**
 * 查询处理器接口
 * <p>
 * 负责处理特定类型的查询并返回结果
 *
 * @param <Q> 查询请求类型
 * @param <R> 查询结果类型
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * 处理查询请求
     *
     * @param query 查询请求对象
     * @return 查询结果
     */
    R handle(Q query);

    /**
     * 获取处理器支持的查询类型
     *
     * @return 查询类型的Class对象
     */
    Class<Q> getQueryType();
}
