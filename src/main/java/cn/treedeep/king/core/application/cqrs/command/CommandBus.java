package cn.treedeep.king.core.application.cqrs.command;

import cn.treedeep.king.core.domain.validation.CommandValidator;
import cn.treedeep.king.core.infrastructure.idempotency.CommandIdempotencyControl;
import cn.treedeep.king.core.infrastructure.monitoring.CommandMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令总线
 * <p>
 * 负责注册和分发命令到对应的处理器。提供了:
 * 1. 命令处理器注册
 * 2. 同步命令分发
 * 3. 命令执行监控
 * 4. 幂等性控制
 */
@Slf4j
@Service
public class CommandBus {

    private final Map<Class<? extends Command>, CommandHandler<?>> handlers = new ConcurrentHashMap<>();
    private final CommandValidator commandValidator;
    private final CommandMetrics commandMetrics;
    private final CommandIdempotencyControl idempotencyControl;

    public CommandBus(CommandValidator commandValidator,
                      CommandMetrics commandMetrics,
                      CommandIdempotencyControl idempotencyControl) {
        this.commandValidator = commandValidator;
        this.commandMetrics = commandMetrics;
        this.idempotencyControl = idempotencyControl;
    }

    /**
     * 注册命令处理器
     *
     * @param commandType 命令类型
     * @param handler     命令处理器
     */
    public void register(Class<? extends Command> commandType, CommandHandler<?> handler) {
        handlers.put(commandType, handler);
        log.info("Registered command handler for command type: {}", commandType.getSimpleName());
    }

    /**
     * 分发命令到对应的处理器
     *
     * @param command 要处理的命令
     * @param <T>     命令类型
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public <T extends Command> void dispatch(T command) {
        long startTime = System.currentTimeMillis();
        String commandType = command.getClass().getSimpleName();

        try {
            // 检查幂等性
            if (idempotencyControl.isDuplicate(command)) {
                log.warn("Duplicate command detected - Type: {}, ID: {}", commandType, command.getCommandId());
                return;
            }

            // 验证命令
            commandValidator.validate(command);

            // 获取处理器
            CommandHandler<T> handler = (CommandHandler<T>) handlers.get(command.getClass());
            if (handler == null) {
                throw new IllegalStateException("No handler registered for command: " + commandType);
            }

            // 执行命令
            handler.handle(command);

            // 记录成功指标
            commandMetrics.recordSuccess(commandType, System.currentTimeMillis() - startTime);
            log.debug("Successfully processed command: {}", commandType);

        } catch (Exception e) {
            // 记录失败指标
            commandMetrics.recordFailure(commandType, System.currentTimeMillis() - startTime);
            log.error("Failed to process command: {} - {}", commandType, e.getMessage());

            // 清除幂等性记录，允许重试
            idempotencyControl.clearIdempotencyRecord(command);
            throw e;
        }
    }


    /**
     * 异步处理命令
     *
     * @param command 要处理的命令
     * @return 异步结果
     */
    @Async("commandExecutor")
    @Transactional
    public <T extends Command> CompletableFuture<Void> dispatchAsync(T command) {
        return CompletableFuture.runAsync(() -> {
            try {
                this.dispatch(command);
            } catch (Exception e) {
                log.error("Failed to process command asynchronously: {} - {}",
                        command.getClass().getSimpleName(), e.getMessage(), e);
                throw e;
            }
        });
    }
}
