package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;

import cn.treedeep.king.core.domain.AbstractRepository;
import cn.treedeep.king.core.domain.DomainEventPublisher;
import cn.treedeep.king.core.domain.EventStore;
import cn.treedeep.king.${moduleNameLower}.domain.AggregateRepository;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleNameCamel}「聚合仓储实现（主要）」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Primary
@Repository("${moduleNameLower}AggregateRepository")
public class AggregateRepositoryImpl extends AbstractRepository<${moduleNameCamel}, ${moduleNameCamel}Id> implements AggregateRepository {

    @Resource(name = "${moduleNameLower}AggregateJpaRepository")
    private AggregateRepository aggregateRepository;

    public AggregateRepositoryImpl(
            CacheManager cacheManager,
            EventStore eventStore,
            DomainEventPublisher eventPublisher) {
        super(cacheManager, eventStore, eventPublisher);
    }

    @Override
    protected Optional<${moduleNameCamel}> doLoad(${moduleNameCamel}Id id) {
        return aggregateRepository.findById(id);
    }

    @Override
    protected void doSave(${moduleNameCamel} aggregate) {
        aggregateRepository.save(aggregate);
    }

    @Override
    public Optional<${moduleNameCamel}> findByName(String name) {
        return aggregateRepository.findByName(name);
    }

    @Override
    public List<${moduleNameCamel}> findAll() {
        return aggregateRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return aggregateRepository.existsByName(name);
    }
}
