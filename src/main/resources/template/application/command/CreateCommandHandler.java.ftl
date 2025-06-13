package ${packageName}.${moduleNameLower}.application.command;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.domain.EventBus;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Item;
import ${packageName}.${moduleNameLower}.domain.service.${entityNameCamel}DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * 创建${moduleComment}命令「处理器」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
@Transactional
@Slf4j
public class Create${entityNameCamel}CommandHandler extends AbstractCommandHandler<Create${entityNameCamel}Command, ${entityNameCamel}> {

    private final ${entityNameCamel}AggregateRepository ${entityNameLower}Repository;
    private final ${entityNameCamel}DomainService ${entityNameLower}DomainService;

    protected Create${entityNameCamel}CommandHandler(EventBus eventBus,
                                                     CommandBus commandBus,
                                                     ${entityNameCamel}AggregateRepository ${entityNameLower}Repository,
                                                     ${entityNameCamel}DomainService ${entityNameLower}DomainService) {
        super(${entityNameLower}Repository, eventBus, commandBus);
        this.${entityNameLower}Repository = ${entityNameLower}Repository;
        this.${entityNameLower}DomainService = ${entityNameLower}DomainService;
    }

    @Override
    protected ${entityNameCamel} handleCommand(Create${entityNameCamel}Command command) {
        log.info("Processing Create${entityNameCamel}Command: {}", command.getName());

        // 验证业务规则
        if (!${entityNameLower}DomainService.isNameUnique(command.getName())) {
            throw new IllegalArgumentException("${entityNameLower} name already exists: " + command.getName());
        }

        // 创建聚合根
        ${entityNameCamel}Id id = ${entityNameCamel}Id.generate();
        ${entityNameCamel}Item ${entityNameLower} = ${entityNameCamel}Item.create(command.getName() + "子项", command.getDescription());

        ${entityNameCamel} ${entityNameLower}Aggregate = new ${entityNameCamel}(id, "测试${moduleComment}", ${entityNameLower});

        log.info("${entityNameCamel} created successfully: {}", id);
        return ${entityNameLower}Aggregate;
    }
}
