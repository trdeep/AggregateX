package cn.treedeep.king.core.domain.cqrs;

import java.io.Serializable;

/**
 * 命令基类
 * <p>
 * 所有命令对象都应该继承此类，命令代表系统中的写操作。
 * Domain层的核心命令定义。
 */
public abstract class Command implements Serializable {
    private final String commandId;
    private final long timestamp;

    protected Command() {
        this.commandId = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public String getCommandId() {
        return commandId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取聚合根ID
     * 子类必须实现此方法以标识命令要操作的聚合根
     */
    public abstract String getAggregateId();
}
