package cn.treedeep.king.core.domain;

import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 抽象仓储实现基类
 * <p>
 * 提供聚合根持久化的通用功能实现，包括：
 * <ul>
 * <li>二级缓存支持 - 提高查询性能</li>
 * <li>乐观锁并发控制 - 防止并发冲突</li>
 * <li>事务管理 - 确保数据一致性</li>
 * <li>事件存储 - 支持事件溯源</li>
 * <li>领域事件发布 - 支持事件驱动架构</li>
 * </ul>
 * <p>
 * 子类需要实现具体的持久化逻辑：
 * <ul>
 * <li>{@link #doLoad(Identifier)} - 从存储中加载聚合根</li>
 * <li>{@link #doSave(AggregateRoot)} - 将聚合根保存到存储</li>
 * </ul>
 *
 * @param <T> 聚合根类型
 * @param <ID> 聚合根标识符类型
 */
@RequiredArgsConstructor
public abstract class AbstractRepository<T extends AggregateRoot<ID>, ID extends Identifier> implements AggregateRepository<T, ID> {

    /**
     * 缓存管理器
     */
    private final CacheManager cacheManager;

    /**
     * 事件存储
     */
    private final EventStore eventStore;

    /**
     * 领域事件发布器
     */
    private final DomainEventPublisher eventPublisher;

    /**
     * 获取缓存名称
     *
     * @return 缓存名称，默认为"aggregates"
     */
    protected String getCacheName() {
        return "aggregates";
    }

    /**
     * 从持久化存储中加载实体
     *
     * @param id 聚合根标识符
     * @return 包含聚合根的Optional，如果不存在则为空
     */
    protected abstract Optional<T> doLoad(ID id);

    /**
     * 持久化实体到存储
     *
     * @param aggregate 要保存的聚合根
     */
    @Transactional
    protected abstract void doSave(T aggregate);

    /**
     * 根据ID查找聚合根
     *
     * @param id 聚合根标识符
     * @return 包含聚合根的Optional，如果不存在则为空
     */
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        // 1. 尝试从一级缓存获取
        Cache.ValueWrapper cached = getCache().get(id.toString());
        if (cached != null) {
            return Optional.of((T) cached.get());
        }

        // 2. 从存储中加载
        Optional<T> aggregate = doLoad(id);

        // 3. 放入缓存
        aggregate.ifPresent(agg -> getCache().put(id.toString(), agg));

        return aggregate;
    }

    @Transactional
    @Override
    public T save(T aggregate) {
        List<DomainEvent> domainEvents = aggregate.getDomainEvents();

        try {
            // 1. 保存聚合根
            doSave(aggregate);

            // 2. 保存领域事件
            eventStore.saveEvents(
                    aggregate.getId().toString(),
                    domainEvents,
                    aggregate.getVersion().intValue()
            );

            // 3. 发布领域事件
            eventPublisher.publishAll(domainEvents);

            // 4. 更新缓存
            getCache().put(aggregate.getId().toString(), aggregate);

            // 5. 清理已处理的事件
            aggregate.clearDomainEvents();

            return aggregate;
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrencyConflictException(
                    String.format("Concurrent modification of aggregate %s with id %s",
                            aggregate.getClass().getSimpleName(),
                            aggregate.getId())
            );
        }
    }

    /**
     * 删除聚合根（软删除）
     *
     * @param aggregate 要删除的聚合根
     */
    @Transactional
    public void delete(T aggregate) {
        // 1. 标记为已删除
        aggregate.markAsDeleted();

        // 2. 保存删除状态
        save(aggregate);

        // 3. 从缓存中移除
        getCache().evict(aggregate.getId().toString());
    }

    private Cache getCache() {
        return cacheManager.getCache(getCacheName());
    }
}
