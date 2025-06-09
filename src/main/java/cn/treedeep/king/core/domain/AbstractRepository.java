package cn.treedeep.king.core.domain;

import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 通用仓储实现
 * <p>
 * 提供:
 * 1. 二级缓存支持
 * 2. 乐观锁并发控制
 * 3. 事务管理
 *
 * @param <T>  聚合根类型
 * @param <ID> 标识符类型
 */
@RequiredArgsConstructor
public abstract class AbstractRepository<T extends AggregateRoot<ID>, ID extends Identifier> implements Repository<T, ID> {

    private final CacheManager cacheManager;
    private final EventStore eventStore;
    private final DomainEventPublisher eventPublisher;

    /**
     * 获取缓存名称
     */
    protected String getCacheName() {
        return "aggregates";
    }

    /**
     * 从持久化存储中加载实体
     */
    protected abstract Optional<T> doLoad(ID id);

    /**
     * 持久化实体到存储
     */
    protected abstract void doSave(T aggregate);

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
