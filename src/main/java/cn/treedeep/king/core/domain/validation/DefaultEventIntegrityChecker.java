package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.core.domain.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 默认事件完整性检查器实现
 */
@Component
public class DefaultEventIntegrityChecker implements EventIntegrityChecker {

    @Override
    public void checkIntegrity(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        LocalDateTime previousTimestamp = null;
        Long previousVersion = null;
        String aggregateId = events.getFirst().getAggregateId();

        for (DomainEvent event : events) {
            // 检查必填字段
            if (event.getEventId() == null || event.getEventId().isEmpty()) {
                throw new ValidationException("事件ID不能为空");
            }
            if (event.getAggregateId() == null || event.getAggregateId().isEmpty()) {
                throw new ValidationException("聚合根ID不能为空");
            }
            if (event.getOccurredOn() == null) {
                throw new ValidationException("事件发生时间不能为空");
            }
            if (event.getAggregateVersion() == null) {
                throw new ValidationException("聚合根版本不能为空");
            }

            // 检查聚合根ID一致性
            if (!event.getAggregateId().equals(aggregateId)) {
                throw new ValidationException("事件流中包含不同聚合根的事件");
            }

            // 检查事件时间顺序
            if (previousTimestamp != null && event.getOccurredOn().isBefore(previousTimestamp)) {
                throw new ValidationException("事件时间顺序不正确");
            }

            // 检查版本连续性
            if (previousVersion != null && event.getAggregateVersion() != previousVersion + 1) {
                throw new ValidationException(
                    String.format("事件版本不连续: 期望 %d, 实际 %d",
                        previousVersion + 1,
                        event.getAggregateVersion())
                );
            }

            previousTimestamp = event.getOccurredOn();
            previousVersion = event.getAggregateVersion();
        }
    }
}
