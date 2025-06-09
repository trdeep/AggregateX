package cn.treedeep.king.core.domain.cqrs;

/**
 * 命令处理器接口
 * <p>
 * 负责处理特定类型的命令，实现写操作的业务逻辑。
 *
 * @param <T> 命令类型
 * @param <R> 命令执行结果类型
 */
public interface CommandHandler<T extends Command, R> {
    /**
     * 处理命令
     *
     * @param command 要处理的命令
     * @return 命令执行结果
     */
    R handle(T command);
}
