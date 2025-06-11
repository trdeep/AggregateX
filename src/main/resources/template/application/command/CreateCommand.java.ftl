package cn.treedeep.king.${moduleNameLower}.application.command;

import cn.treedeep.king.core.application.cqrs.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * 【创建${moduleComment}】命令
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RequiredArgsConstructor
@Getter
public class Create${moduleNameCamel}Command extends Command {

    private final String name;
    private final String description;

    @Override
    public String getAggregateId() {
        return name;
    }
}
