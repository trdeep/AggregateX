package cn.treedeep.king.${moduleNameLower}.infrastructure;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.${moduleNameLower}.application.command.Create${moduleNameCamel}Command;
import cn.treedeep.king.${moduleNameLower}.application.command.Create${moduleNameCamel}CommandHandler;
import cn.treedeep.king.${moduleNameLower}.application.query.${moduleNameCamel}ListQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}「模块配置」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Configuration("ModuleConfig.${moduleNameCamel}")
@RequiredArgsConstructor
public class ModuleConfig {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final ${moduleNameCamel}ListQueryHandler ${moduleNameLower}ListQueryHandler;
    private final Create${moduleNameCamel}CommandHandler create${moduleNameCamel}CommandHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void registerHandlers() {
        // 注册命令处理器
        commandBus.register(Create${moduleNameCamel}Command.class, create${moduleNameCamel}CommandHandler);

        // 注册查询处理器
        queryBus.register(${moduleNameLower}ListQueryHandler);
    }
}
