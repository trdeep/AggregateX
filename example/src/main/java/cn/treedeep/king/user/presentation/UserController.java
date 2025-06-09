package cn.treedeep.king.user.presentation;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.user.application.command.LoginUserCommand;
import cn.treedeep.king.user.application.command.RegisterUserCommand;
import cn.treedeep.king.user.application.dto.UserStatusDto;
import cn.treedeep.king.user.application.query.UserListQuery;
import cn.treedeep.king.user.application.query.UserListQueryResult;
import cn.treedeep.king.user.presentation.dto.LoginRequest;
import cn.treedeep.king.user.presentation.dto.RegisterRequest;
import cn.treedeep.king.user.presentation.dto.UserListResponse;
import cn.treedeep.king.shared.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理REST控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "用户管理", description = "用户注册、登录、查询等功能")
public class UserController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    public Result register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求: username={}, email={}", request.getUsername(), request.getEmail());

        RegisterUserCommand command = new RegisterUserCommand(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        commandBus.dispatch(command);

        return Result.success(request);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录验证")
    public Result login(@Valid @RequestBody LoginRequest request,
                                     HttpServletRequest httpRequest) {
        log.info("用户登录请求: usernameOrEmail={}", request.getUsernameOrEmail());

        String clientIp = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        LoginUserCommand command = new LoginUserCommand(
                request.getUsernameOrEmail(),
                request.getPassword(),
                clientIp,
                userAgent
        );

        commandBus.dispatch(command);

        return Result.success();
    }

    @GetMapping
    @Operation(summary = "查询用户列表", description = "分页查询用户列表，支持多种过滤条件")
    public ResponseEntity<UserListResponse> getUserList(
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱（模糊查询）") @RequestParam(required = false) String email,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatusDto status,
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向（asc/desc）") @RequestParam(defaultValue = "desc") String sortDirection) {

        log.info("查询用户列表: username={}, email={}, status={}, page={}, size={}",
                username, email, status, page, size);

        UserListQuery query = new UserListQuery();
        query.setUsername(username);
        query.setEmail(email);
        query.setStatus(status);
        query.setPage(page);
        query.setSize(size);
        query.setSortBy(sortBy);
        query.setSortDirection(sortDirection);

        UserListQueryResult result = queryBus.execute(query);

        UserListResponse response = new UserListResponse();
        response.setUsers(result.getUsers());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        response.setCurrentPage(result.getCurrentPage());
        response.setPageSize(result.getPageSize());
        response.setHasNext(result.isHasNext());
        response.setHasPrevious(result.isHasPrevious());

        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
