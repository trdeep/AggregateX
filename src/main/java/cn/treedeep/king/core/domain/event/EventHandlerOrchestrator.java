package cn.treedeep.king.core.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 事件处理编排器
 * <p>
 * 负责管理和协调事件处理器的执行：
 * 1. 支持顺序和并行执行
 * 2. 提供重试机制
 * 3. 错误处理和监控
 */
@Slf4j
@Component
public class EventHandlerOrchestrator {

    private final Map<String, HandlerExecutionOrder> handlerRegistry = new ConcurrentHashMap<>();
    private final ExecutorService executorService;
    private final RetryStrategy retryStrategy;

    public EventHandlerOrchestrator(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
        this.executorService = Executors.newWorkStealingPool();
    }

    /**
     * 注册事件处理器
     */
    public void register(String eventType, List<EventHandler<?>> handlers, HandlerExecutionOrder.ExecutionOrder order) {
        handlerRegistry.put(eventType, new HandlerExecutionOrder(eventType, handlers, order));
        log.info("注册事件处理器: eventType={}, handlers={}, order={}", eventType, handlers.size(), order);
    }

    /**
     * 处理事件
     */
    @SuppressWarnings("unchecked")
    public void processEvent(DomainEvent event) {
        String eventType = event.getClass().getName();
        HandlerExecutionOrder executionOrder = handlerRegistry.get(eventType);

        if (executionOrder == null) {
            log.warn("没有找到事件处理器: eventType={}", eventType);
            return;
        }

        try {
            if (executionOrder.order() == HandlerExecutionOrder.ExecutionOrder.PARALLEL) {
                processParallel(event, executionOrder.handlers());
            } else {
                processSequential(event, executionOrder.handlers());
            }
        } catch (Exception e) {
            log.error("事件处理失败: eventType={}, error={}", eventType, e.getMessage(), e);
            throw e;
        }
    }

    private void processSequential(DomainEvent event, List<EventHandler<?>> handlers) {
        for (EventHandler handler : handlers) {
            executeWithRetry(event, handler);
        }
    }

    private void processParallel(DomainEvent event, List<EventHandler<?>> handlers) {
        try {
            handlers.stream()
                .map(handler -> executorService.submit(() -> executeWithRetry(event, handler)))
                .forEach(future -> {
                    try {
                        future.get(30, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        throw new RuntimeException("并行处理事件超时", e);
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException("并行处理事件失败", e);
        }
    }

    private void executeWithRetry(DomainEvent event, EventHandler handler) {
        int attempts = 0;
        Exception lastException = null;

        while (true) {
            try {
                handler.handle(event);
                return;
            } catch (Exception e) {
                lastException = e;
                attempts++;

                if (!retryStrategy.shouldRetry(event, e, attempts)) {
                    log.error("事件处理重试次数超限: eventType={}, handler={}, attempts={}",
                        event.getClass().getSimpleName(), handler.getClass().getSimpleName(), attempts);
                    throw new RuntimeException("事件处理失败", lastException);
                }

                long delay = retryStrategy.getNextDelay(attempts);
                log.warn("事件处理失败，准备重试: eventType={}, handler={}, attempt={}, delay={}ms",
                    event.getClass().getSimpleName(), handler.getClass().getSimpleName(), attempts, delay);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断", ie);
                }
            }
        }
    }
}
