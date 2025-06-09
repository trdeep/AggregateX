package cn.treedeep.king.core.infrastructure.eventstore.compression;

import cn.treedeep.king.core.domain.DomainEvent;

import java.util.List;

/**
 * 事件压缩器接口
 * <p>
 * 用于压缩和解压事件流，以减少存储空间和提高性能
 */
public interface EventCompressor {
    /**
     * 压缩事件列表
     *
     * @param events 要压缩的事件列表
     * @return 压缩后的事件列表
     */
    List<DomainEvent> compress(List<DomainEvent> events);

    /**
     * 判断是否需要压缩
     *
     * @param events 事件列表
     * @return true如果事件列表需要压缩
     */
    boolean shouldCompress(List<DomainEvent> events);

    /**
     * 获取压缩阈值（事件数量）
     *
     * @return 压缩阈值
     */
    int compressionThreshold();
}
