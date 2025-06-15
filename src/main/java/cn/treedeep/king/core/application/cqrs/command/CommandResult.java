package cn.treedeep.king.core.application.cqrs.command;

import lombok.Data;

@Data
public class CommandResult<T> {
    private Command command;
    private T result;
    private long timestamp;

    public CommandResult(Command command, T result) {
        this.command = command;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }
}
