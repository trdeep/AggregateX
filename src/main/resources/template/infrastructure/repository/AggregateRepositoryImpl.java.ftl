package ${packageName}.${moduleNameLower}.infrastructure.repository;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import ${packageName}.${moduleNameLower}.domain.repository.${entityNameCamel}AggregateRepository;
import cn.treedeep.king.core.domain.AbstractRepository;
import cn.treedeep.king.core.domain.DomainEventPublisher;
import cn.treedeep.king.core.domain.EventStore;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「聚合根仓储实现（主要）」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Primary
@Repository("${entityNameLower}AggregateRepository")
public class ${entityNameCamel}AggregateRepositoryImpl extends AbstractRepository<${entityNameCamel}, ${entityNameCamel}Id> implements ${entityNameCamel}AggregateRepository {

    @Resource(name = "${entityNameLower}AggregateJpaRepository")
    private ${entityNameCamel}AggregateJpaRepository aggregateRepository;

    public ${entityNameCamel}AggregateRepositoryImpl(
            CacheManager cacheManager,
            EventStore eventStore,
            DomainEventPublisher eventPublisher) {
        super(cacheManager, eventStore, eventPublisher);
    }

    @Override
    protected Optional<${entityNameCamel}> doLoad(${entityNameCamel}Id id) {
        return aggregateRepository.findById(id);
    }

    @Override
    protected void doSave(${entityNameCamel} aggregate) {
        aggregateRepository.save(aggregate);
    }

    @Override
    public List<${entityNameCamel}> findAll() {
        return aggregateRepository.findAll();
    }

}
