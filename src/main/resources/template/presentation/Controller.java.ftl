package cn.treedeep.king.${moduleNameLower}.presentation;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.${moduleNameLower}.application.command.Create${moduleNameCamel}Command;
import cn.treedeep.king.${moduleNameLower}.application.query.${moduleNameCamel}ListQuery;
import cn.treedeep.king.${moduleNameLower}.application.query.${moduleNameCamel}ListQueryResult;
import cn.treedeep.king.${moduleNameLower}.presentation.dto.Create${moduleNameCamel}Request;
import cn.treedeep.king.${moduleNameLower}.presentation.dto.${moduleNameCamel}ListResponse;
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
 * ${moduleComment} REST 控制器
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@RestController
@RequestMapping("/api/${moduleNameLower}s")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "${moduleNameCamel} Management", description = "${moduleComment}管理接口")
public class ${moduleNameCamel}Controller {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "创建${moduleComment}", description = "创建新的${moduleComment}")
    @PostMapping
    public ResponseEntity<Result> create${moduleNameCamel}(@Valid @RequestBody Create${moduleNameCamel}Request request) {
        log.info("Creating ${moduleNameCamel}: {}", request.getName());

        Create${moduleNameCamel}Command command = new Create${moduleNameCamel}Command(
                request.getName(),
                request.getDescription()
        );

        commandBus.dispatch(command);
        return ResponseEntity.ok(Result.success());
    }

    @Operation(summary = "获取${moduleComment}列表", description = "分页获取${moduleComment}列表")
    @GetMapping
    public ResponseEntity<Result> get${moduleNameCamel}List(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {

        log.info("Getting ${moduleNameLower} list: page={}, size={}, name={}", page, size, name);

        ${moduleNameCamel}ListQuery query = new ${moduleNameCamel}ListQuery(page, size, name);
        ${moduleNameCamel}ListQueryResult queryResult = queryBus.execute(query);

        ${moduleNameCamel}ListResponse response = new ${moduleNameCamel}ListResponse(queryResult.get${moduleNameCamel}s());
        return ResponseEntity.ok(Result.success(response));
    }
}
