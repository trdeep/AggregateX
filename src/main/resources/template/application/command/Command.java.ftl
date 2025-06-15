package ${packageName}.${moduleNameLower}.application.command;

import cn.treedeep.king.core.application.cqrs.command.Command;
import lombok.Getter;

@Getter
public class SayHelloCommand extends Command {
    private final String name;

    public SayHelloCommand(String name) {
        this.name = name;
    }
}
