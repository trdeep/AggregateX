package cn.treedeep.king.core.domain;

import java.util.List;

/**
 * 事件存储接口
 * <p>
 * 定义了事件存储的基本操作
 */
public interface EventStore {

    /**
     * 保存聚合根产生的领域事件
     *
     * @param aggregateId     聚合根ID
     * @param events          要保存的领域事件列表
     * @param expectedVersion 期望的聚合根版本号，用于乐观锁控制
     */
    void saveEvents(String aggregateId, List<DomainEvent> events, int expectedVersion);

    /**
     * 获取特定聚合根的所有历史事件
     *
     * @param aggregateId 聚合根ID
     * @return 该聚合根的所有历史事件列表
     */
    List<DomainEvent> getEvents(String aggregateId);

    /**
     * 获取系统中的所有事件
     * 主要用于事件溯源、调试和审计
     *
     * @return 所有历史事件列表
     */
    List<DomainEvent> getAllEvents();
}
