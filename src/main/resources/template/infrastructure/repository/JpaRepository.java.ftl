package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;


import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Item;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} 仓储接口
 * </p>
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Repository("${moduleNameLower}JpaRepository")
public interface ${moduleNameCamel}JpaRepository extends JpaRepository<${moduleNameCamel}Item, ${moduleNameCamel}Id> {

}
