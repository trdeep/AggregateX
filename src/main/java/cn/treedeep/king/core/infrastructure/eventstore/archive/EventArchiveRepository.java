package cn.treedeep.king.core.infrastructure.eventstore.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 事件归档仓储接口
 */
@Repository
public interface EventArchiveRepository extends JpaRepository<ArchivedEvent, Long> {

    /**
     * 查找指定时间段内的归档事件
     */
    @Query("SELECT e FROM ArchivedEvent e WHERE e.archivedAt BETWEEN :startDate AND :endDate")
    List<ArchivedEvent> findByArchiveDateBetween(
        @Param("startDate") OffsetDateTime startDate,
        @Param("endDate") OffsetDateTime endDate);

    /**
     * 查找指定聚合根的归档事件
     */
    List<ArchivedEvent> findByAggregateId(String aggregateId);

    /**
     * 查找某个聚合根在指定版本之前的归档事件
     */
    @Query("SELECT e FROM ArchivedEvent e WHERE e.aggregateId = :aggregateId AND e.aggregateVersion <= :version")
    List<ArchivedEvent> findByAggregateIdAndVersionLessThanEqual(
        @Param("aggregateId") String aggregateId,
        @Param("version") Long version);
}
