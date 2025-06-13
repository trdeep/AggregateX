package ${packageName}.${moduleNameLower}.infrastructure.repository;


import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}Item「实体仓储（JPA实现）」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Repository("${entityNameLower}JpaRepository")
public interface ${entityNameCamel}JpaRepository extends JpaRepository<${entityNameCamel}Item, ${entityNameCamel}Id> {

}
