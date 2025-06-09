package cn.treedeep.king.core.application.cqrs.query;

/**
 * 应用层查询
 * <p>
 * 继承自领域层的查询基类,提供应用层面的查询功能扩展
 *
 * @param <T> 查询结果类型
 */
public abstract class Query<T> extends cn.treedeep.king.core.domain.cqrs.Query {

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getResultType() {
        return (Class<T>) getClass();
    }
}
