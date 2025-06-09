package cn.treedeep.king.core.domain;

import org.jmolecules.ddd.types.Identifier;

import java.util.List;

/**
 * 聚合根工厂接口
 * <p>
 * 负责创建和重建聚合根实例。工厂方法确保聚合根的创建过程符合领域规则，
 * 同时提供了一个统一的接口来处理聚合根的初始化和重建。
 *
 * @param <T>  聚合根类型
 * @param <ID> 聚合根标识符类型
 */
public interface AggregateFactory<T extends AggregateRoot<ID>, ID extends Identifier> {

    /**
     * 创建新的聚合根实例
     *
     * @param id 聚合根标识符
     * @return 新创建的聚合根实例
     * @throws IllegalArgumentException 如果标识符无效
     */
    T create(ID id);

    /**
     * 从事件流中重建聚合根
     *
     * @param id     聚合根标识符
     * @param events 历史事件列表
     * @return 重建后的聚合根实例
     * @throws IllegalArgumentException 如果标识符无效或事件列表为空
     */
    T reconstitute(ID id, List<DomainEvent> events);
}
