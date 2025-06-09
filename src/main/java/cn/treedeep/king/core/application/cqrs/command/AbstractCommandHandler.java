package cn.treedeep.king.core.application.cqrs.command;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.core.domain.Repository;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 抽象命令处理器基类
 * <p>
 * 提供了处理命令和发布事件的基础设施，是CQRS中命令处理的核心组件。
 * <p>
 * 职责：<br>
 * 1. 处理特定类型的命令<br>
 * 2. 自动管理事务边界<br>
 * 3. 保存聚合根状态<br>
 * 4. 发布领域事件
 */
public abstract class AbstractCommandHandler<T extends Command, A extends AggregateRoot<?>> implements CommandHandler<T> {

    private final Repository<A, ?> repository;
    private final EventBus eventBus;
    private final CommandBus commandBus;

    protected AbstractCommandHandler(Repository<A, ?> repository, EventBus eventBus, CommandBus commandBus) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.commandBus = commandBus;

        // 在构造时自动注册到命令总线
        commandBus.register(getCommandType(), this);
    }

    /**
     * 获取命令类型
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getCommandType() {
        return (Class<T>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(
                getClass(), AbstractCommandHandler.class
        ))[0];
    }

    @Override
    @Transactional
    public void handle(T command) {
        // 执行具体的命令处理逻辑
        A aggregate = handleCommand(command);

        // 保存聚合根
        repository.save(aggregate);

        // 发布所有新产生的领域事件
        for (DomainEvent event : aggregate.getDomainEvents()) {
            eventBus.publish(event);
        }

        // 清除已发布的事件
        aggregate.clearDomainEvents();
    }

    /**
     * 处理具体的命令
     *
     * @param command 要处理的命令
     * @return 更新后的聚合根
     */
    protected abstract A handleCommand(T command);

    public CommandBus getCommandBus() {
        return commandBus;
    }
}
