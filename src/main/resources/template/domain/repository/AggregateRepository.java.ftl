package ${packageName}.${moduleNameLower}.domain.repository;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「仓储接口」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
public interface ${entityNameCamel}AggregateRepository extends cn.treedeep.king.core.domain.AggregateRepository<${entityNameCamel}, ${entityNameCamel}Id> {

    List<${entityNameCamel}> findAll();

    // TODO 添加需要的仓储接口方法
}
