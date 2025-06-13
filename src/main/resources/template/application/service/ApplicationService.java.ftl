package ${packageName}.${moduleNameLower}.application.service;

import ${packageName}.${moduleNameLower}.application.dto.${moduleNameCamel}Dto;
import ${packageName}.${moduleNameLower}.application.dto.${moduleNameCamel}DtoConverter;
import ${packageName}.${moduleNameLower}.domain.${moduleNameCamel}AggregateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}「应用服务」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Service
@AllArgsConstructor
@Transactional
public class ${moduleNameCamel}ApplicationService {

    private final ${moduleNameCamel}AggregateRepository aggregateRepository;
    private final ${moduleNameCamel}DtoConverter ${moduleNameLower}DtoConverter;

    public List<${moduleNameCamel}Dto> findAll() {
        return aggregateRepository.findAll()
                .stream()
                .map(${moduleNameLower}DtoConverter::toDto)
                .toList();
    }
}
