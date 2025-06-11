package cn.treedeep.king.core.infrastructure.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.shared.utils.SpringBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 领域事件日志过滤器
 * <p>
 * 为领域事件相关的日志添加额外的上下文信息
 */
public class DomainEventLogFilter extends Filter<ILoggingEvent> {
    private final ObjectMapper objectMapper = SpringBeanUtil.getBean("defaultObjectMapper", ObjectMapper.class);

    @Override
    public FilterReply decide(ILoggingEvent event) {
        try {
            // 尝试解析日志消息中的领域事件
            if (event.getArgumentArray() != null) {
                for (Object arg : event.getArgumentArray()) {
                    if (arg instanceof DomainEvent domainEvent) {
                        enrichEventContext(domainEvent);
                    }
                }
            }
        } catch (Exception e) {
            // 记录过滤器错误但不影响日志输出
            addErrorContext(e);
        }

        return FilterReply.NEUTRAL;
    }

    private void enrichEventContext(DomainEvent event) {
        MDC.put("eventId", event.getEventId());
        MDC.put("eventType", event.getClass().getSimpleName());
        MDC.put("aggregateId", event.getAggregateId());
        MDC.put("aggregateVersion", String.valueOf(event.getAggregateVersion()));
        MDC.put("occurredOn", event.getOccurredOn().toString());
        MDC.put("traceId", getOrCreateTraceId());
    }

    private void addErrorContext(Exception e) {
        MDC.put("filterError", e.getMessage());
        MDC.put("filterErrorType", e.getClass().getSimpleName());
    }

    private String getOrCreateTraceId() {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }
        return traceId;
    }
}
