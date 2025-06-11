package cn.treedeep.king.${moduleNameLower}.application.service;

import cn.treedeep.king.${moduleNameLower}.application.dto.${moduleNameCamel}Dto;
import cn.treedeep.king.${moduleNameLower}.application.dto.${moduleNameCamel}DtoConverter;
import cn.treedeep.king.${moduleNameLower}.domain.AggregateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ${moduleNameCamel}ApplicationService {

    private final AggregateRepository aggregateRepository;
    private final ${moduleNameCamel}DtoConverter ${moduleNameLower}DtoConverter;

    public List<${moduleNameCamel}Dto> findAll() {
        return aggregateRepository.findAll()
                .stream()
                .map(${moduleNameLower}DtoConverter::toDto)
                .toList();
    }
}
