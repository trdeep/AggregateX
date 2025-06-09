package cn.treedeep.king.user.application.query;

import cn.treedeep.king.user.application.dto.UserDto;
import cn.treedeep.king.user.application.service.UserDtoMapperService;
import cn.treedeep.king.user.domain.User;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户列表查询结果
 */
@Data
public class UserListQueryResult {

    public UserListQueryResult(Page<User> page) {
        // 转换为DTO
        List<UserDto> userDtos = page.getContent().stream()
                .map(UserDtoMapperService::toDto)
                .collect(Collectors.toList());

        this.setUsers(userDtos);
        this.setTotalElements(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
        this.setCurrentPage(page.getNumber());
        this.setPageSize(page.getSize());
        this.setHasNext(page.hasNext());
        this.setHasPrevious(page.hasPrevious());
    }

    /**
     * 用户列表
     */
    private List<UserDto> users;

    /**
     * 总数量
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 当前页码
     */
    private int currentPage;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;
}
