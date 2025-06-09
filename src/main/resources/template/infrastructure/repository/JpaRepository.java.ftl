package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;

import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} JPA 仓储
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Repository("${moduleNameLower}JpaRepository")
public interface ${moduleNameCamel}JpaRepository extends JpaRepository<${moduleNameCamel}, ${moduleNameCamel}Id>, ${moduleNameCamel}Repository {

}
