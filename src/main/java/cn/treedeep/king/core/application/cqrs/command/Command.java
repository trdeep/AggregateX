package cn.treedeep.king.core.application.cqrs.command;

import lombok.extern.slf4j.Slf4j;

/**
 * 应用层命令
 * <p>
 * 继承自领域层的命令基类,提供应用层面的命令功能扩展
 */
@Slf4j
public abstract class Command extends cn.treedeep.king.core.domain.cqrs.Command {

    @Override
    public String getAggregateId() {
        log.warn("请在子类中实现getAggregateId方法");
        return "";
    }
}
