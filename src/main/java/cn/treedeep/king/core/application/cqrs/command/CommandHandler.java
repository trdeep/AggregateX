package cn.treedeep.king.core.application.cqrs.command;

import java.util.concurrent.CompletableFuture;

/**
 * 命令处理器接口
 * <p>
 * 负责处理特定类型的命令
 *
 * @param <T> 要处理的命令类型
 */
public interface CommandHandler<T extends Command, R> {

    /**
     * 处理命令
     *
     * @param command 要处理的命令对象
     */
    void handle(T command, CompletableFuture<CommandResult<R>> future);

    Class<? extends Command> getCommandType();


}
