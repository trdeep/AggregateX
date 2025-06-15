package ${packageName}.${moduleNameLower}.application.service;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};

import java.util.List;
import java.util.Optional;

public interface ApplicationService {

    Optional<${entityNameCamel}> findById(String id);

    List<${entityNameCamel}> fandAll();
}
