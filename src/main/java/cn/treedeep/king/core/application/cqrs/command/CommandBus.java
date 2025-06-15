package cn.treedeep.king.core.application.cqrs.command;

import cn.treedeep.king.core.domain.validation.AbstractCommandValidator;
import cn.treedeep.king.core.infrastructure.idempotency.CommandIdempotencyControl;
import cn.treedeep.king.core.infrastructure.monitoring.CommandMetrics;
import cn.treedeep.king.shared.properties.CqrsProperties;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令总线
 * <p>
 * 作为CQRS架构中的核心组件，负责将命令路由到相应的处理器执行。
 * 提供了命令执行的统一入口，并集成了多种横切关注点。
 * <p>
 * 主要功能：
 * <ul>
 * <li>命令处理器注册和管理</li>
 * <li>命令路由和分发</li>
 * <li>同步和异步命令执行</li>
 * <li>命令验证和前置检查</li>
 * <li>幂等性控制</li>
 * <li>性能监控和指标收集</li>
 * <li>异常处理和日志记录</li>
 * </ul>
 * <p>
 * 设计特点：
 * <ul>
 * <li>类型安全 - 编译时验证命令和处理器的匹配</li>
 * <li>线程安全 - 支持多线程环境下的并发访问</li>
 * <li>可扩展性 - 支持插件式的功能扩展</li>
 * <li>监控友好 - 内置指标收集和链路追踪</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * {@code
 * // 执行命令
 * commandBus.execute(new CreateOrderCommand(customerId, items));
 *
 * // 异步执行命令
 * CompletableFuture<Void> future = commandBus.executeAsync(command);
 * }
 * </pre>
 */
@Slf4j
@Service
public class CommandBus {

    private final Map<Class<? extends Command>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    private final CommandMetrics commandMetrics;
    private final CommandIdempotencyControl idempotencyControl;

    private final Validator validator;

    /**
     * 设置是否启用快速失败模式
     * <p>
     * 在快速失败模式下，发现第一个验证错误时就会抛出异常
     */
    private final boolean failFast;

    /**
     * 设置是否启用验证
     * <p>
     * 如果禁用验证，validate方法将直接返回而不进行任何验证
     */
    private final boolean validationEnabled;


    @Resource
    private Set<? extends AbstractCommandValidator> commandValidators;

    /**
     * 构造命令总线
     *
     * @param commandMetrics     命令指标收集器
     * @param idempotencyControl 幂等性控制器
     */
    public CommandBus(CqrsProperties properties, Validator validator, CommandMetrics commandMetrics, CommandIdempotencyControl idempotencyControl) {
        this.commandMetrics = commandMetrics;
        this.idempotencyControl = idempotencyControl;
        this.validator = validator;
        this.failFast = properties.getValidation().isFailFast();
        this.validationEnabled = properties.getValidation().isValidationEnabled();
    }


    /**
     * 注册命令处理器
     *
     * @param commandType 命令类型
     * @param handler     命令处理器
     */
    public void register(Class<? extends Command> commandType, CommandHandler<?, ?> handler) {
        handlers.put(commandType, handler);
        log.info("Registered command handler for command type: {}", commandType.getSimpleName());
    }

    public void register(CommandHandler<?, ?> handler) {
        handlers.put(handler.getCommandType(), handler);
    }

    /**
     * 分发命令到对应的处理器
     *
     * @param command 要处理的命令
     * @param <T>     命令类型
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public <T extends Command, R> CompletableFuture<CommandResult<R>> dispatch(T command) {
        CompletableFuture<CommandResult<R>> future = new CompletableFuture<>();

        long startTime = System.currentTimeMillis();
        String commandType = command.getClass().getSimpleName();

        try {
            // 检查幂等性
            if (idempotencyControl.isDuplicate(command)) {
                log.warn("Duplicate command detected - Type: {}, ID: {}", commandType, command.getCommandId());
                return CompletableFuture.failedFuture(new IllegalStateException("Duplicate command detected"));
            }

            // 验证命令
            commandValidators.stream()
                    .filter(v -> v.getClass().getSimpleName().startsWith(command.getClass().getSimpleName()))
                    .forEach(v -> v.doValidate(validator, validationEnabled, failFast, command));

            // 获取处理器
            CommandHandler<T, R> handler = (CommandHandler<T, R>) handlers.get(command.getClass());
            if (handler == null) {
                return CompletableFuture.failedFuture(new IllegalStateException("No handler registered for command: " + commandType));
            }

            // 执行命令
            handler.handle(command, future);

            // 记录成功指标
            commandMetrics.recordSuccess(commandType, System.currentTimeMillis() - startTime);
            log.debug("Successfully processed command: {}", commandType);

            return future;

        } catch (Exception e) {
            // 记录失败指标
            commandMetrics.recordFailure(commandType, System.currentTimeMillis() - startTime);
            log.error("Failed to process command: {} - {}", commandType, e.getMessage());

            // 清除幂等性记录，允许重试
            idempotencyControl.clearIdempotencyRecord(command);
            return CompletableFuture.failedFuture(e);
        }
    }

}
