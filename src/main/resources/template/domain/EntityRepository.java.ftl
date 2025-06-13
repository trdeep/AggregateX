package ${packageName}.${moduleNameLower}.domain;

import java.util.List;
import java.util.Optional;

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

 /**
 * 根据ID查找${entityNameCamel}
 */
 Optional<${entityNameCamel}> findById(${entityNameCamel}Id id);

 /**
 * 根据名称查找${entityNameCamel}
 */
 Optional<${entityNameCamel}> findByName(String name);

 /**
 * 查询所有${entityNameCamel}
 */
 List<${entityNameCamel}> findAll();

 /**
 * 检查名称是否存在
 */
 boolean existsByName(String name);
 }
