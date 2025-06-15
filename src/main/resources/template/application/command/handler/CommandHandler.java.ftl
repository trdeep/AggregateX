package ${packageName}.${moduleNameLower}.application.command.handler;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.command.CommandResult;
import cn.treedeep.king.core.domain.EventBus;
import ${packageName}.${moduleNameLower}.application.command.SayHelloCommand;
import ${packageName}.${moduleNameLower}.domain.repository.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class SayHelloCommandHandler extends AbstractCommandHandler<SayHelloCommand, ${entityNameCamel}, ${entityNameCamel}AggregateRepository, Object> {

    protected SayHelloCommandHandler(${entityNameCamel}AggregateRepository repository, EventBus eventBus, CommandBus commandBus) {
        super(repository, eventBus, commandBus);
    }

    @Override
    protected ${entityNameCamel} doHandle(SayHelloCommand command, CompletableFuture<CommandResult<Object>> future) {
        Optional<${entityNameCamel}> ${entityNameLower} = aggregateRepository.findById(UserId.of(command.getAggregateId()));
        future.complete(new CommandResult<>(command, "异步结果：" + command.getName()));
        ${entityNameLower}.ifPresent(u -> u.sayHello(command.getName()));
        return ${entityNameLower}.orElse(null);
    }
}
