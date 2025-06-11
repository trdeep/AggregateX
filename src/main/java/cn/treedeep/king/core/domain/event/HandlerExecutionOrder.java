package cn.treedeep.king.core.domain.event;

import cn.treedeep.king.core.domain.EventHandler;

import java.util.List;

/**
 * 事件处理器的执行顺序配置
 * <p>
 * 定义了特定事件类型的处理器执行顺序和方式。
 * 支持顺序执行和并行执行两种模式。
 *
 * @param eventType 事件类型的完全限定名
 * @param handlers  处理该事件的处理器列表
 * @param order     执行顺序配置
 */
public record HandlerExecutionOrder(String eventType, List<EventHandler<?>> handlers, ExecutionOrder order) {

    /**
     * 执行顺序枚举
     */
    public enum ExecutionOrder {
        /**
         * 按顺序执行 - 前一个处理器完成后才执行下一个
         */
        SEQUENTIAL,
        /**
         * 并行执行 - 所有处理器同时执行
         */
        PARALLEL
    }
}
