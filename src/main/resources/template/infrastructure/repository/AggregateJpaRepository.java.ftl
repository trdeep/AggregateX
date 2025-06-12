package ${packageName}.${moduleNameLower}.infrastructure.repository;

import ${packageName}.${moduleNameLower}.domain.AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.${moduleNameCamel};
import ${packageName}.${moduleNameLower}.domain.${moduleNameCamel}Id;
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
