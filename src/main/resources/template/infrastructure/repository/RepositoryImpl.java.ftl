package cn.treedeep.king.${moduleNameLower}.infrastructure.repository;

import cn.treedeep.king.core.domain.AbstractRepository;
import cn.treedeep.king.core.domain.DomainEventPublisher;
import cn.treedeep.king.core.domain.EventStore;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel};
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Id;
import cn.treedeep.king.${moduleNameLower}.domain.${moduleNameCamel}Repository;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
* Copyright © ${copyright} 版权所有
* <p>
* ${moduleNameCamel} 仓储实现
* </p>
*
* @author ${author}
* @since ${dateTime}
*/
@Primary
@Repository
public class ${moduleNameCamel}RepositoryImpl extends AbstractRepository<${moduleNameCamel}, ${moduleNameCamel}Id> implements ${moduleNameCamel}Repository {

    @Resource(name = "${moduleNameLower}JpaRepository")
    private ${moduleNameCamel}Repository ${moduleNameLower}JpaRepository;

    public ${moduleNameCamel}RepositoryImpl(
            CacheManager cacheManager,
            EventStore eventStore,
            DomainEventPublisher eventPublisher) {
        super(cacheManager, eventStore, eventPublisher);
    }

    @Override
    protected Optional<${moduleNameCamel}> doLoad(${moduleNameCamel}Id id) {
        return ${moduleNameLower}JpaRepository.findById(id);
    }

    @Override
    protected void doSave(${moduleNameCamel} aggregate) {
        ${moduleNameLower}JpaRepository.save(aggregate);
    }

    @Override
    public Optional<${moduleNameCamel}> findByName(String name) {
        return ${moduleNameLower}JpaRepository.findByName(name);
    }

    @Override
    public List<${moduleNameCamel}> findAll() {
        return ${moduleNameLower}JpaRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return ${moduleNameLower}JpaRepository.existsByName(name);
    }
}
