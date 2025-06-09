package cn.treedeep.king.user.application.query;

import cn.treedeep.king.user.domain.UserStatus;
import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 用户查询参数
 */
@Data
public class UserQueryParams {

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
    private UserStatus status;

    /**
     * 分页参数
     */
    private Pageable pageable;

    public UserQueryParams(String username, String email, UserStatus status, Pageable pageable) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.pageable = pageable;
    }
}
