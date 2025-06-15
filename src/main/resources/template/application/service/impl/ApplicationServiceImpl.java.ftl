package ${packageName}.${moduleNameLower}.application.service.impl;

import ${packageName}.${moduleNameLower}.application.service.ApplicationService;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import ${packageName}.${moduleNameLower}.domain.repository.${entityNameCamel}AggregateRepository;
import ${packageName}.${moduleNameLower}.domain.service.DomainService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service("${moduleNameLower}_ApplicationService")
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private DomainService domainService;

    @Override
    public Optional<${entityNameCamel}> getOne(String name) {
        ${entityNameCamel} ${entityNameLower} = new ${entityNameCamel}();
        ${entityNameLower}.setId(new ${entityNameCamel}Id());

        // 实际上应该从数据库获取

        log.info("应用服务：创建实体, {}", name);
        return Optional.of(${entityNameLower});
    }

    @Override
    public List<${entityNameCamel}> fandAll() {
        return domainService.findAll();
    }
}
