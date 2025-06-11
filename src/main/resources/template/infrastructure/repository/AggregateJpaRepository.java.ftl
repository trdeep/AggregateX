package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;

import cn.treedeep.king.${moduleNameLower}.domain.AggregateRepository;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel}「聚合仓储（JPA 实现）」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Repository("${moduleNameLower}AggregateJpaRepository")
public interface AggregateJpaRepository extends JpaRepository<${moduleNameCamel}, ${moduleNameCamel}Id>, AggregateRepository {

}
