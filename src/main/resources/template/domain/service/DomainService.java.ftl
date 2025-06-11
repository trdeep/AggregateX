package cn.treedeep.king.${moduleNameLower}.domain.service;

import cn.treedeep.king.${moduleNameLower}.domain.AggregateRepository;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel} 领域服务
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Service
@RequiredArgsConstructor
public class ${moduleNameCamel}DomainService {

    private final AggregateRepository ${moduleNameLower}Repository;

    /**
     * 验证${moduleNameCamel}名称唯一性
     */
    public boolean isNameUnique(String name) {
        return !${moduleNameLower}Repository.existsByName(name);
    }

    /**
     * 验证${moduleNameCamel}是否可以删除
     */
    public boolean canBeDeleted(${moduleNameCamel} ${moduleNameLower}) {
        // 实现业务规则检查
        return true;
    }
}
