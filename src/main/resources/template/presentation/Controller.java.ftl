package ${packageName}.${moduleNameLower}.presentation;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import ${packageName}.${moduleNameLower}.application.command.Create${entityNameCamel}Command;
import ${packageName}.${moduleNameLower}.application.query.${entityNameCamel}ListQuery;
import ${packageName}.${moduleNameLower}.application.query.${entityNameCamel}ListQueryResult;
import ${packageName}.${moduleNameLower}.presentation.dto.Create${entityNameCamel}Request;
import ${packageName}.${moduleNameLower}.presentation.dto.${entityNameCamel}ListResponse;
import cn.treedeep.king.shared.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${moduleComment}「REST 控制器」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RestController
@RequestMapping("/api/${entityNameLower}s")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "${entityNameCamel} Management", description = "${moduleComment}管理接口")
public class ${entityNameCamel}Controller {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "创建${moduleComment}", description = "创建新的${moduleComment}")
    @PostMapping
    public ResponseEntity<Result> create${entityNameCamel}(@Valid @RequestBody Create${entityNameCamel}Request request) {
        log.info("Creating ${entityNameCamel}: {}", request.getName());

        Create${entityNameCamel}Command command = new Create${entityNameCamel}Command(
                request.getName(),
                request.getDescription()
        );

        commandBus.dispatch(command);
        return ResponseEntity.ok(Result.success());
    }

    @Operation(summary = "获取${moduleComment}列表", description = "分页获取${moduleComment}列表")
    @GetMapping
    public ResponseEntity<Result> get${entityNameCamel}List(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {

        log.info("Getting ${entityNameLower} list: page={}, size={}, name={}", page, size, name);

        ${entityNameCamel}ListQuery query = new ${entityNameCamel}ListQuery(page, size, name);
        ${entityNameCamel}ListQueryResult queryResult = queryBus.execute(query);

        ${entityNameCamel}ListResponse response = new ${entityNameCamel}ListResponse(queryResult.get${entityNameCamel}s());
        return ResponseEntity.ok(Result.success(response));
    }
}
