package cn.treedeep.king.core.application.cqrs.command;

import cn.treedeep.king.core.domain.AggregateRepository;
import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventBus;
import lombok.Getter;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
 *
 * @param <C> 命令类型，必须继承Command
 * @param <A> 聚合根类型，必须继承AggregateRoot
 */
@Getter
public abstract class AbstractCommandHandler<C extends Command, A extends AggregateRoot<? extends Identifier>, AR extends AggregateRepository<A, ? extends Identifier>, R> implements CommandHandler<C, R> {
    private final Class<C> commandType;
    protected final AR aggregateRepository;
    protected final EventBus eventBus;
    protected final CommandBus commandBus;

    /**
     * 构造函数
     *
     * @param repository 聚合根仓储
     * @param eventBus   事件总线
     * @param commandBus 命令总线
     */
    @SuppressWarnings("unchecked")
    protected AbstractCommandHandler(AR repository, EventBus eventBus, CommandBus commandBus) {
        this.aggregateRepository = repository;
        this.eventBus = eventBus;
        this.commandBus = commandBus;
        this.commandType = (Class<C>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(getClass(), AbstractCommandHandler.class))[0];

        // 在构造时自动注册到命令总线
        commandBus.register(this);
    }


    @Override
    @Transactional
    public void handle(C command, CompletableFuture<CommandResult<R>> future) {
        // 执行具体的命令处理逻辑
        A aggregate = doHandle(command, future);

        if (aggregate == null) {
            return;
        }

        // 保存聚合根
        aggregateRepository.save(aggregate);

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
    protected abstract A doHandle(C command, CompletableFuture<CommandResult<R>> future);

}
