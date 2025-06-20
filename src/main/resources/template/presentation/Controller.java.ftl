package ${packageName}.${moduleNameLower}.presentation;

import ${packageName}.${moduleNameLower}.application.query.SayHelloQuery;
import ${packageName}.${moduleNameLower}.application.query.result.ListQueryResult;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.command.CommandResult;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import ${packageName}.${moduleNameLower}.application.command.SayHelloCommand;
import cn.treedeep.king.shared.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/say")
@AllArgsConstructor
@Slf4j
@Tag(name = "SayHello Management", description = "👋你好～")
public class SayHelloController {

    private CommandBus commandBus;
    private QueryBus queryBus;

    @Operation(summary = "创建对话", description = "say hello")
    @GetMapping("/hello")
    public ResponseEntity<Result> hello(@Parameter String name) {
        CompletableFuture<CommandResult<String>> dispatch = commandBus.dispatch(new SayHelloCommand(name));
        String result = dispatch.getNow(null).getResult();
        return ResponseEntity.ok(Result.success(result));
    }

    @Operation(summary = "获取对话列表", description = "list")
    @GetMapping("/records")
    public ResponseEntity<ListQueryResult> records() {
        ListQueryResult queryResult = queryBus.execute(new SayHelloQuery("查询条件"));
        return ResponseEntity.ok(queryResult);
    }
}
