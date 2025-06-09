package cn.treedeep.king.core.application.cqrs.query;

import java.util.List;
import java.util.Optional;

/**
 * 查询服务接口
 * <p>
 * CQRS模式中的查询端服务，负责处理所有的查询操作
 *
 * @param <T> 查询结果类型
 * @param <ID> 标识符类型
 */
public interface QueryService<T, ID> {

    /**
     * 根据ID查询单个实体
     *
     * @param id 实体标识符
     * @return 实体，如果不存在则返回空
     */
    Optional<T> findById(ID id);

    /**
     * 查询所有实体
     *
     * @return 实体列表
     */
    List<T> findAll();

    /**
     * 根据条件查询
     *
     * @param criteria 查询条件
     * @return 符合条件的实体列表
     */
    List<T> findByCriteria(QueryCriteria criteria);

    /**
     * 分页查询
     *
     * @param criteria 查询条件
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    QPage<T> findPageByCriteria(QueryCriteria criteria, int page, int size);
}
