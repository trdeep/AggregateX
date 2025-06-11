package cn.treedeep.king.${moduleNameLower}.domain;

import java.util.List;
import java.util.Optional;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} 仓储接口
 * </p>
 *
 * @author ${author}
 * @since ${dateTime}
 */
public interface AggregateRepository extends cn.treedeep.king.core.domain.AggregateRepository<${moduleNameCamel}, ${moduleNameCamel}Id> {

    /**
     * 根据ID查找${moduleNameCamel}
     */
    Optional<${moduleNameCamel}> findById(${moduleNameCamel}Id id);

    /**
     * 根据名称查找${moduleNameCamel}
     */
    Optional<${moduleNameCamel}> findByName(String name);

    /**
     * 查询所有${moduleNameCamel}
     */
    List<${moduleNameCamel}> findAll();

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);
}
