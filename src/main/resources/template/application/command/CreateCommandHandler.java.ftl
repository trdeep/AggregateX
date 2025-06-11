package cn.treedeep.king.${moduleNameLower}.application.command;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.${moduleNameLower}.domain.*;
import cn.treedeep.king.${moduleNameLower}.domain.service.${moduleNameCamel}DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * 创建${moduleComment}命令处理器
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Component
@Slf4j
@Transactional
public class Create${moduleNameCamel}CommandHandler extends AbstractCommandHandler<Create${moduleNameCamel}Command, ${moduleNameCamel}> {

    private final AggregateRepository ${moduleNameLower}Repository;
    private final ${moduleNameCamel}DomainService ${moduleNameLower}DomainService;

    protected Create${moduleNameCamel}CommandHandler(
            EventBus eventBus,
            CommandBus commandBus,
            AggregateRepository ${moduleNameLower}Repository,
            ${moduleNameCamel}DomainService ${moduleNameLower}DomainService) {
        super(${moduleNameLower}Repository, eventBus, commandBus);
        this.${moduleNameLower}Repository = ${moduleNameLower}Repository;
        this.${moduleNameLower}DomainService = ${moduleNameLower}DomainService;
    }

    @Override
    protected ${moduleNameCamel} handleCommand(Create${moduleNameCamel}Command command) {
        log.info("Processing Create${moduleNameCamel}Command: {}", command.getName());

        // 验证业务规则
        if (!${moduleNameLower}DomainService.isNameUnique(command.getName())) {
            throw new IllegalArgumentException("${moduleNameCamel} name already exists: " + command.getName());
        }

        // 创建聚合根
        ${moduleNameCamel}Id id = ${moduleNameCamel}Id.generate();
        ${moduleNameCamel}Item ${moduleNameLower} = ${moduleNameCamel}Item.create(command.getName() + "子项", command.getDescription());

        ${moduleNameCamel} ${moduleNameLower}Aggregate = new ${moduleNameCamel}(id, "测试${moduleComment}", ${moduleNameLower});

        log.info("${moduleNameCamel} created successfully: {}", id);
        return ${moduleNameLower}Aggregate;
    }
}
