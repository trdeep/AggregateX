package cn.treedeep.king.core.infrastructure.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 快照仓储接口
 */
@Repository
public interface SnapshotRepository extends JpaRepository<AggregateSnapshot, String> {

    /**
     * 根据聚合ID和类型查找最新的快照
     */
    AggregateSnapshot findTopByAggregateIdAndAggregateTypeOrderByVersionDesc(
            String aggregateId, String aggregateType);
}
