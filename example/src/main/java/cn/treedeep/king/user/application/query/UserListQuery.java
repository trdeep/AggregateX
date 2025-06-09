package cn.treedeep.king.user.application.query;

import cn.treedeep.king.core.application.cqrs.query.Query;
import cn.treedeep.king.user.application.dto.UserStatusDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户列表查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListQuery extends Query<UserListQueryResult> {

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 邮箱（模糊查询）
     */
    private String email;

    /**
     * 用户状态
     */
    private UserStatusDto status;

    /**
     * 页码（从0开始）
     */
    private int page = 0;

    /**
     * 每页大小
     */
    private int size = 20;

    /**
     * 排序字段
     */
    private String sortBy = "createdAt";

    /**
     * 排序方向（asc/desc）
     */
    private String sortDirection = "desc";

    @Override
    public Class<UserListQueryResult> getResultType() {
        return UserListQueryResult.class;
    }

    @Override
    public String getQueryName() {
        return "UserListQuery";
    }
}
