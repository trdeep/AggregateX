package cn.treedeep.king.core.infrastructure.eventstore.compression;

import cn.treedeep.king.core.domain.DomainEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认事件压缩器实现
 * <p>
 * 使用以下策略进行事件压缩：<br>
 * 1. 合并连续的相同类型事件<br>
 * 2. 删除被后续事件覆盖的状态变更<br>
 * 3. 保留所有重要的业务事件
 */
@Slf4j
public record DefaultEventCompressor(int compressionThreshold) implements EventCompressor {

    @Override
    public List<DomainEvent> compress(List<DomainEvent> events) {
        if (!shouldCompress(events)) {
            return events;
        }

        log.debug("开始压缩事件流，原始事件数量: {}", events.size());

        Map<String, DomainEvent> latestStateByProperty = new HashMap<>();
        List<DomainEvent> compressedEvents = new ArrayList<>();

        // 遍历事件，识别和合并可压缩的事件
        for (DomainEvent event : events) {
            if (isCompressible(event)) {
                // 如果是状态变更事件，只保留最新的状态
                updateLatestState(latestStateByProperty, event);
            } else {
                // 如果是重要的业务事件，直接保留
                compressedEvents.add(event);
            }
        }

        // 添加所有最新的状态变更
        compressedEvents.addAll(latestStateByProperty.values());

        log.debug("事件流压缩完成，压缩后事件数量: {}", compressedEvents.size());
        return compressedEvents;
    }

    @Override
    public boolean shouldCompress(List<DomainEvent> events) {
        return events.size() > compressionThreshold;
    }


    /**
     * 判断事件是否可以压缩
     * 某些重要的业务事件不应该被压缩
     */
    private boolean isCompressible(DomainEvent event) {
        // 通过注解或其他方式标记的不可压缩事件
        if (event.getClass().isAnnotationPresent(NonCompressible.class)) {
            return false;
        }

        // 默认认为状态变更事件是可压缩的
        return event.getClass().getSimpleName().endsWith("StatusChangedEvent");
    }

    /**
     * 更新属性的最新状态
     */
    private void updateLatestState(Map<String, DomainEvent> latestStateByProperty, DomainEvent event) {
        // 使用事件类型作为属性键
        String propertyKey = event.getClass().getName();
        latestStateByProperty.put(propertyKey, event);
    }
}
