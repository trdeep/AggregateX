package cn.treedeep.king.user.application.query;

import cn.treedeep.king.core.application.cqrs.query.AbstractQueryHandler;
import cn.treedeep.king.user.application.dto.UserDtoConverter;
import cn.treedeep.king.user.domain.User;
import cn.treedeep.king.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 用户列表查询处理器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserListQueryHandler extends AbstractQueryHandler<UserListQuery, UserListQueryResult> {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    @Override
    public UserListQueryResult doHandle(UserListQuery query) {
        log.info("处理用户列表查询: {}", query);

        // 构建分页和排序
        Sort sort = createSort(query.getSortBy(), query.getSortDirection());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

        // 执行查询
        Page<User> userPage = executeQuery(query, pageable);

        // 构建结果
        UserListQueryResult result = new UserListQueryResult(userPage);

        log.info("用户列表查询完成，返回{}条记录", result.getTotalElements());
        return result;
    }


    @Override
    public Class<UserListQuery> getQueryType() {
        return UserListQuery.class;
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }

    private Page<User> executeQuery(UserListQuery query, Pageable pageable) {
        // 如果没有任何过滤条件，返回所有用户
        if (isEmptyFilter(query)) {
            return userRepository.findAll(pageable);
        }

        // 将DTO状态转换为领域对象状态
        var domainStatus = userDtoConverter.toDomain(query.getStatus());

        // 如果有复合条件，使用复合查询
        return userRepository.findByConditions(
                query.getUsername(),
                query.getEmail(),
                domainStatus,
                pageable
        );
    }

    private boolean isEmptyFilter(UserListQuery query) {
        return (query.getUsername() == null || query.getUsername().trim().isEmpty()) &&
                (query.getEmail() == null || query.getEmail().trim().isEmpty()) &&
                query.getStatus() == null;
    }
}
