package ${packageName}.${moduleNameLower}.domain.service;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「领域服务」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Service
@RequiredArgsConstructor
public class ${entityNameCamel}DomainService {

    private final ${entityNameCamel}AggregateRepository ${entityNameLower}Repository;

    /**
     * 验证${entityNameCamel}名称唯一性
     */
    public boolean isNameUnique(String name) {
        return !${entityNameLower}Repository.existsByName(name);
    }

    /**
     * 验证${entityNameCamel}是否可以删除
     */
    public boolean canBeDeleted(${entityNameCamel} ${entityNameLower}) {
        // 实现业务规则检查
        return true;
    }
}
