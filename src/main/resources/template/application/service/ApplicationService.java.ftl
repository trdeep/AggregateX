package ${packageName}.${moduleNameLower}.application.service;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}DtoConverter;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}AggregateRepository;
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
public class ${entityNameCamel}ApplicationService {

    private final ${entityNameCamel}AggregateRepository aggregateRepository;
    private final ${entityNameCamel}DtoConverter ${entityNameLower}DtoConverter;

    public List<${entityNameCamel}Dto> findAll() {
        return aggregateRepository.findAll()
                .stream()
                .map(${entityNameLower}DtoConverter::toDto)
                .toList();
    }
}
