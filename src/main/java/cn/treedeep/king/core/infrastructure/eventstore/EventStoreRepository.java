package cn.treedeep.king.core.infrastructure.eventstore;

import cn.treedeep.king.core.domain.DomainEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 事件存储仓储接口
 * <p>
 * 提供对事件存储的数据访问能力
 */
@Repository
public interface EventStoreRepository extends JpaRepository<DomainEvent, String> {

    /**
     * 根据聚合根ID查找其所有事件
     *
     * @param aggregateId 聚合根ID
     * @return 该聚合根的所有历史事件列表
     */
    List<DomainEvent> findByAggregateId(String aggregateId);

    /**
     * 根据事件发生时间查找其所有事件
     *
     * @param cutoffDate 查找截止时间
     * @return 该时间点之前的所有事件列表
     */
    List<DomainEvent> findByOccurredOnBefore(OffsetDateTime cutoffDate);
}
