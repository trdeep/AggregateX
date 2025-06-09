package cn.treedeep.king.user.presentation;

import cn.treedeep.king.core.application.cqrs.query.QPage;
import cn.treedeep.king.core.application.cqrs.query.QueryCriteria;
import cn.treedeep.king.user.application.dto.UserDto;
import cn.treedeep.king.user.application.dto.UserDtoConverter;
import cn.treedeep.king.user.application.dto.UserIdDto;
import cn.treedeep.king.user.application.dto.UserStatusDto;
import cn.treedeep.king.user.application.query.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserQueryService 使用示例控制器
 * <p>
 * 展示如何使用 QueryService 接口的标准化查询方法
 */
@RestController
@RequestMapping("/api/example/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "用户查询示例", description = "演示 UserQueryService 的各种用法")
public class UserQueryRestController {

    private final UserQueryService userQueryService;
    private final UserDtoConverter userDtoConverter;

    /**
     * 示例1：根据ID查询单个用户
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "展示 findById 方法的使用")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "用户ID") @PathVariable String id) {

        log.info("查询用户，ID: {}", id);

        var userId = userDtoConverter.toDomain(UserIdDto.of(id));
        Optional<UserDto> user = userQueryService.findById(userId);

        return user.map(userDto -> {
            log.info("找到用户: {}", userDto.getUsername());
            return ResponseEntity.ok(userDto);
        }).orElseGet(() -> {
            log.warn("用户不存在，ID: {}", id);
            return ResponseEntity.notFound().build();
        });
    }

    /**
     * 示例2：查询所有用户
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有用户", description = "展示 findAll 方法的使用")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        log.info("查询所有用户");

        List<UserDto> users = userQueryService.findAll();

        log.info("找到 {} 个用户", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * 示例3：根据条件查询用户（不分页）
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询用户", description = "展示 findByCriteria 方法的使用")
    public ResponseEntity<List<UserDto>> searchUsers(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatusDto status,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDirection) {

        log.info("条件查询用户: username={}, email={}, status={}", username, email, status);

        QueryCriteria criteria = buildQueryCriteria(username, email, status, sortBy, sortDirection);
        List<UserDto> users = userQueryService.findByCriteria(criteria);

        log.info("条件查询结果: 找到 {} 个用户", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * 示例4：分页查询用户
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "展示 findPageByCriteria 方法的使用")
    public ResponseEntity<QPage<UserDto>> getPagedUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatusDto status,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDirection) {

        log.info("分页查询用户: page={}, size={}, username={}, email={}, status={}",
                page, size, username, email, status);

        QueryCriteria criteria = buildQueryCriteria(username, email, status, sortBy, sortDirection);
        QPage<UserDto> userPage = userQueryService.findPageByCriteria(criteria, page, size);

        log.info("分页查询结果: 第{}页，共{}条记录，共{}页",
                userPage.getCurrentPage() + 1, userPage.getTotalElements(), userPage.getTotalPages());

        return ResponseEntity.ok(userPage);
    }

    /**
     * 示例5：活跃用户查询
     */
    @GetMapping("/active")
    @Operation(summary = "查询活跃用户", description = "查询状态为ACTIVE的用户")
    public ResponseEntity<List<UserDto>> getActiveUsers(
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "lastLoginAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDirection) {

        log.info("查询活跃用户");

        QueryCriteria criteria = new QueryCriteria();
        criteria.addFilter("status", userDtoConverter.toDomain(UserStatusDto.ACTIVE))
                .addSort(sortBy, sortDirection);

        List<UserDto> activeUsers = userQueryService.findByCriteria(criteria);

        log.info("找到 {} 个活跃用户", activeUsers.size());
        return ResponseEntity.ok(activeUsers);
    }

    /**
     * 示例6：最近登录用户（分页）
     */
    @GetMapping("/recent-login")
    @Operation(summary = "最近登录用户", description = "查询最近有登录记录的用户")
    public ResponseEntity<QPage<UserDto>> getRecentLoginUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size) {

        log.info("查询最近登录用户: page={}, size={}", page, size);

        QueryCriteria criteria = new QueryCriteria();
        criteria.addSort("lastLoginAt", "desc")
                .addSort("loginCount", "desc");

        QPage<UserDto> userPage = userQueryService.findPageByCriteria(criteria, page, size);

        log.info("最近登录用户查询结果: 第{}页，共{}条记录",
                userPage.getCurrentPage() + 1, userPage.getTotalElements());

        return ResponseEntity.ok(userPage);
    }

    /**
     * 示例7：用户名模糊搜索
     */
    @GetMapping("/search-username/{keyword}")
    @Operation(summary = "用户名模糊搜索", description = "根据关键词搜索用户名")
    public ResponseEntity<List<UserDto>> searchByUsername(
            @Parameter(description = "搜索关键词") @PathVariable String keyword) {

        log.info("用户名模糊搜索: {}", keyword);

        QueryCriteria criteria = new QueryCriteria();
        criteria.addFilter("username", keyword)
                .addSort("username", "asc");

        List<UserDto> users = userQueryService.findByCriteria(criteria);

        log.info("用户名搜索结果: 找到 {} 个匹配用户", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * 示例8：复合条件查询
     */
    @GetMapping("/complex-search")
    @Operation(summary = "复合条件查询", description = "演示多个条件组合查询")
    public ResponseEntity<QPage<UserDto>> complexSearch(
            @Parameter(description = "用户名关键词") @RequestParam(required = false) String usernameKeyword,
            @Parameter(description = "邮箱关键词") @RequestParam(required = false) String emailKeyword,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatusDto status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "15") int size) {

        log.info("复合条件查询: username={}, email={}, status={}, page={}, size={}",
                usernameKeyword, emailKeyword, status, page, size);

        QueryCriteria criteria = new QueryCriteria();

        // 添加多个过滤条件
        if (usernameKeyword != null && !usernameKeyword.trim().isEmpty()) {
            criteria.addFilter("username", usernameKeyword.trim());
        }
        if (emailKeyword != null && !emailKeyword.trim().isEmpty()) {
            criteria.addFilter("email", emailKeyword.trim());
        }
        if (status != null) {
            criteria.addFilter("status", userDtoConverter.toDomain(status));
        }

        // 添加多级排序
        criteria.addSort("status", "asc")  // 首先按状态排序
                .addSort("lastLoginAt", "desc")  // 然后按最后登录时间排序
                .addSort("username", "asc");     // 最后按用户名排序

        QPage<UserDto> userPage = userQueryService.findPageByCriteria(criteria, page, size);

        log.info("复合条件查询结果: 第{}页，共{}条记录，匹配条件数量: {}",
                userPage.getCurrentPage() + 1, userPage.getTotalElements(),
                criteria.getFilters().size());

        return ResponseEntity.ok(userPage);
    }

    /**
     * 构建查询条件的辅助方法
     */
    private QueryCriteria buildQueryCriteria(String username, String email, UserStatusDto status,
                                             String sortBy, String sortDirection) {
        QueryCriteria criteria = new QueryCriteria();

        // 添加过滤条件
        if (username != null && !username.trim().isEmpty()) {
            criteria.addFilter("username", username.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            criteria.addFilter("email", email.trim());
        }
        if (status != null) {
            criteria.addFilter("status", userDtoConverter.toDomain(status));
        }

        // 添加排序
        criteria.addSort(sortBy, sortDirection);

        return criteria;
    }
}
