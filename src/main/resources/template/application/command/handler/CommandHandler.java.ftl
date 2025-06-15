package ${packageName}.${moduleNameLower}.application.command.handler;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.command.CommandResult;
import cn.treedeep.king.core.domain.AggregateRepository;
import cn.treedeep.king.core.domain.EventBus;
import ${packageName}.${moduleNameLower}.application.command.SayHelloCommand;
import ${packageName}.${moduleNameLower}.application.service.ApplicationService;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class SayHelloCommandHandler extends AbstractCommandHandler<SayHelloCommand, ${entityNameCamel}, String> {

    @Resource
    private ApplicationService applicationService;

    /**
     * 构造函数
     *
     * @param repository 聚合根仓储，用于持久化操作
     * @param eventBus   事件总线，用于发布事件
     * @param commandBus 命令总线，用于注册当前命令
     */
    protected SayHelloCommandHandler(AggregateRepository<${entityNameCamel}, ${entityNameCamel}Id> repository,
                                     EventBus eventBus, CommandBus commandBus,
                                     ApplicationService applicationService) {
        super(repository, eventBus, commandBus);
        this.applicationService = applicationService;
    }

    @Override
    protected ${entityNameCamel} doHandle(SayHelloCommand command, CompletableFuture<CommandResult<String>> future) {
        Optional<${entityNameCamel}> ${entityNameLower} = applicationService.getOne(command.getName());
        future.complete(new CommandResult<>(command, "异步结果：" + command.getName()));
        ${entityNameLower}.ifPresent(u -> u.sayHello(command.getName()));
        return ${entityNameLower}.orElse(null);
    }
}
