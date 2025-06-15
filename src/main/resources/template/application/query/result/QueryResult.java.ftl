package ${packageName}.${moduleNameLower}.application.query.result;

import ${packageName}.${moduleNameLower}.application.dto.${entityNameCamel}Dto;
import lombok.Data;

import java.util.List;

@Data
public class ListQueryResult {

    List<${entityNameCamel}Dto> list;
}
