package ${packageName}.${moduleNameLower}.infrastructure.repository;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「聚合仓储（JPA 实现）」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Repository("${entityNameLower}AggregateJpaRepository")
public interface ${entityNameCamel}AggregateJpaRepository extends JpaRepository<${entityNameCamel}, ${entityNameCamel}Id>, ${entityNameCamel}AggregateRepository {

}
