package ${packageName}.${moduleNameLower}.application.dto;

import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
import lombok.Getter;

@Getter
public class ${entityNameCamel}Dto {
    private final String id;

    public ${entityNameCamel}Dto(${entityNameCamel} ${entityNameLower}) {
        this.id = ${entityNameLower}.getId().getValue();
    }
}
