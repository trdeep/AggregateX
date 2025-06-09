package cn.treedeep.king.user.application.query;

import cn.treedeep.king.core.application.cqrs.query.QPage;
import cn.treedeep.king.core.application.cqrs.query.QueryCriteria;
import cn.treedeep.king.core.application.cqrs.query.QueryService;
import cn.treedeep.king.user.application.dto.UserDto;
import cn.treedeep.king.user.application.service.UserDtoMapperService;
import cn.treedeep.king.user.domain.User;
import cn.treedeep.king.user.domain.UserId;
import cn.treedeep.king.user.domain.UserRepository;
import cn.treedeep.king.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户查询服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserQueryService implements QueryService<UserDto, UserId> {

    private final UserRepository userRepository;

    @Override
    public Optional<UserDto> findById(UserId id) {
        log.debug("根据ID查询用户: {}", id.getValue());

        return userRepository.findById(id)
                .map(UserDtoMapperService::toDto);
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("查询所有用户");

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        return userRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(UserDtoMapperService::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findByCriteria(QueryCriteria criteria) {
        log.debug("根据条件查询用户: {}", criteria);

        // 解析查询条件
        String username = (String) criteria.getFilters().get("username");
        String email = (String) criteria.getFilters().get("email");
        UserStatus status = (UserStatus) criteria.getFilters().get("status");

        // 创建分页参数
        Sort sort = createSortFromCriteria(criteria);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        // 执行查询
        Page<User> users = executeQuery(username, email, status, pageable);

        return users.getContent()
                .stream()
                .map(UserDtoMapperService::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QPage<UserDto> findPageByCriteria(QueryCriteria criteria, int page, int size) {
        log.debug("分页查询用户: criteria={}, page={}, size={}", criteria, page, size);

        // 解析查询条件
        String username = (String) criteria.getFilters().get("username");
        String email = (String) criteria.getFilters().get("email");
        UserStatus status = (UserStatus) criteria.getFilters().get("status");

        // 创建分页参数
        Sort sort = createSortFromCriteria(criteria);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 执行查询
        Page<User> users = executeQuery(username, email, status, pageable);

        // 转换为DTO并构建分页结果
        List<UserDto> userDtos = users.getContent()
                .stream()
                .map(UserDtoMapperService::toDto)
                .collect(Collectors.toList());

        return QPage.<UserDto>builder()
                .content(userDtos)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .currentPage(users.getNumber())
                .pageSize(users.getSize())
                .hasNext(users.hasNext())
                .hasPrevious(users.hasPrevious())
                .build();
    }

    /**
     * 分页查询用户（保留原有方法，用于向后兼容）
     */
    public Page<UserQueryResult> queryUsers(UserQueryParams params) {
        log.info("查询用户列表: username={}, email={}, status={}, page={}, size={}",
                params.getUsername(), params.getEmail(), params.getStatus(),
                params.getPageable().getPageNumber(), params.getPageable().getPageSize());

        // 执行查询
        Page<User> users = executeQuery(
                params.getUsername(),
                params.getEmail(),
                params.getStatus(),
                params.getPageable()
        );

        // 转换为DTO
        return users.map(this::convertToDto);
    }

    /**
     * 执行查询
     */
    private Page<User> executeQuery(String username, String email, UserStatus status, Pageable pageable) {
        // 根据查询条件决定使用哪个查询方法
        if (hasComplexConditions(username, email, status)) {
            // 复合条件查询
            return userRepository.findByConditions(username, email, status, pageable);
        } else if (username != null && !username.trim().isEmpty()) {
            // 按用户名模糊查询
            return userRepository.findByUsernameContaining(username.trim(), pageable);
        } else if (email != null && !email.trim().isEmpty()) {
            // 按邮箱模糊查询
            return userRepository.findByEmailValueContaining(email.trim(), pageable);
        } else if (status != null) {
            // 按状态查询
            return userRepository.findByStatus(status, pageable);
        } else {
            // 查询所有用户
            return userRepository.findAll(pageable);
        }
    }

    /**
     * 从查询条件创建排序
     */
    private Sort createSortFromCriteria(QueryCriteria criteria) {
        if (criteria.getSorts() == null || criteria.getSorts().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        List<Sort.Order> orders = criteria.getSorts().entrySet().stream()
                .map(entry -> {
                    Sort.Direction direction = "desc".equalsIgnoreCase(entry.getValue())
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                    return new Sort.Order(direction, entry.getKey());
                })
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

    /**
     * 检查是否有复合查询条件
     */
    private boolean hasComplexConditions(String username, String email, UserStatus status) {
        int conditionCount = 0;
        if (username != null && !username.trim().isEmpty()) {
            conditionCount++;
        }
        if (email != null && !email.trim().isEmpty()) {
            conditionCount++;
        }
        if (status != null) {
            conditionCount++;
        }
        return conditionCount > 1;
    }

    /**
     * 将用户实体转换为DTO
     */
    private UserQueryResult convertToDto(User user) {
        return new UserQueryResult(
                user.getId().getValue(),
                user.getUsername(),
                user.getEmail().getValue(),
                user.getStatus(),
                user.getLastLoginAt(),
                user.getLastLoginIp(),
                user.getLoginCount(),
                user.getCreatedAt(),
                user.getLastModifiedAt()
        );
    }
}
