package ${packageName}.${moduleNameLower}.domain.service.impl;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.event.SayHelloEvent;
import ${packageName}.${moduleNameLower}.domain.repository.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.service.DomainService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「领域服务实现」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Service("${moduleNameLower}_DomainService")
public class DomainServiceImpl implements DomainService {

    @Resource(name = "${entityNameLower}AggregateRepository")
    private ${entityNameCamel}AggregateRepository ${entityNameLower}Repository;

    @Override
    public void sayHello(String name) {

    }

    @Override
    public List<${entityNameCamel}> findAll() {
        return ${entityNameLower}Repository.findAll();
    }

    @EventListener
    public void hello(SayHelloEvent sayHelloEvent) {
        System.out.println("事件监听：hello world.");
    }

    // TODO 请完善领域服务实现
}
