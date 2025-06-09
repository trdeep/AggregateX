package cn.treedeep.king.core.domain.event;

import cn.treedeep.king.core.domain.EventHandler;

import java.util.List;

/**
 * 事件处理器的执行顺序
 */
public record HandlerExecutionOrder(String eventType, List<EventHandler<?>> handlers, ExecutionOrder order) {

    public enum ExecutionOrder {
        SEQUENTIAL,    // 按顺序执行
        PARALLEL      // 并行执行
    }
}
