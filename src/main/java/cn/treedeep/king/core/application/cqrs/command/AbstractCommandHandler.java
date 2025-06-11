package cn.treedeep.king.core.application.cqrs.command;

import cn.treedeep.king.core.domain.AggregateRepository;
import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventBus;
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
 *
 * @param <T> 命令类型，必须继承Command
 * @param <A> 聚合根类型，必须继承AggregateRoot
 */
public abstract class AbstractCommandHandler<T extends Command, A extends AggregateRoot<?>> implements CommandHandler<T> {

    private final AggregateRepository<A, ?> aggregateRepository;
    private final EventBus eventBus;
    private final CommandBus commandBus;

    /**
     * 构造函数
     *
     * @param aggregateRepository 聚合根仓储，用于持久化操作
     * @param eventBus 事件总线，用于发布领域事件
     * @param commandBus 命令总线，用于注册命令处理器
     */
    protected AbstractCommandHandler(AggregateRepository<A, ?> aggregateRepository, EventBus eventBus, CommandBus commandBus) {
        this.aggregateRepository = aggregateRepository;
        this.eventBus = eventBus;
        this.commandBus = commandBus;

        // 在构造时自动注册到命令总线
        commandBus.register(getCommandType(), this);
    }

    /**
     * 获取命令类型
     * <p>
     * 通过泛型类型解析获取当前处理器处理的命令类型
     *
     * @return 命令类型的Class对象
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
    protected abstract A handleCommand(T command);

    /**
     * 获取命令总线实例
     *
     * @return 命令总线
     */
    public CommandBus getCommandBus() {
        return commandBus;
    }
}
