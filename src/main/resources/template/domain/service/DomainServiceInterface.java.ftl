package ${packageName}.${moduleNameLower}.domain.service;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「领域服务接口」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
public interface ${moduleNameCamel}DomainService {

    void sayHello(String name);

    List<${entityNameCamel}> findAll();

    // TODO 请完善领域服务接口
}
