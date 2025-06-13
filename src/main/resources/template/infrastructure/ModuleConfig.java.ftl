package ${packageName}.${moduleNameLower}.infrastructure;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import ${packageName}.${moduleNameLower}.application.command.Create${entityNameCamel}Command;
import ${packageName}.${moduleNameLower}.application.command.Create${entityNameCamel}CommandHandler;
import ${packageName}.${moduleNameLower}.application.query.${entityNameCamel}ListQueryHandler;
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
@Configuration("ModuleConfig.${entityNameCamel}")
@RequiredArgsConstructor
public class ModuleConfig {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final ${entityNameCamel}ListQueryHandler ${moduleNameLower}ListQueryHandler;
    private final Create${entityNameCamel}CommandHandler create${entityNameCamel}CommandHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void registerHandlers() {
        // 注册命令处理器
        commandBus.register(Create${entityNameCamel}Command.class, create${entityNameCamel}CommandHandler);

        // 注册查询处理器
        queryBus.register(${moduleNameLower}ListQueryHandler);
    }
}
