package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;

import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import cn.treedeep.king.${moduleNameLower}.domain.AggregateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} 聚合 JPA仓储
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Repository("${moduleNameLower}AggregateJpaRepository")
public interface AggregateJpaRepository extends JpaRepository<${moduleNameCamel}, ${moduleNameCamel}Id>, AggregateRepository {

}
