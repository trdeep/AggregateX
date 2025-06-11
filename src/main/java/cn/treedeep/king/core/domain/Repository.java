package cn.treedeep.king.core.domain;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 仓储接口
 * <p>
 * DDD中的仓储模式实现，用于持久化和查询聚合根。
 * <p>
 * 使用说明：<br>
 * 1. 每个聚合根类型都应该有自己的仓储接口<br>
 * 2. 仓储应该只与聚合根交互，不直接操作聚合根内部的实体<br>
 * 3. 查询方法的名称应该反映领域概念<br>
 * 4. 复杂查询建议使用专门的查询对象或规范模式
 * <p>
 * 示例：
 * <pre>
 *     {@code
 * public interface OrderRepository extends Repository&lt;Order, OrderId&lt; {
 *      List&lt;Order&lt; findByCustomerId(CustomerId customerId);
 *      Optional&lt;Order&lt; findByOrderNumber(String orderNumber);
 * }
 * }
 * </pre>
 *
 * @param <T>  聚合根类型
 * @param <ID> 聚合根标识符类型
 */
@NoRepositoryBean
public interface Repository<T extends AggregateRoot<ID>, ID extends Identifier> {

    /**
     * 保存聚合根
     * 
     * @param entity 要保存的聚合根实体
     * @return 保存后的聚合根实体
     */
    T save(T entity);
}
