package cn.treedeep.king.user.application.query;

import cn.treedeep.king.user.domain.UserStatus;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 用户查询结果DTO
 */
@Data
public class UserQueryResult {

    private String userId;
    private String username;
    private String email;
    private UserStatus status;
    private OffsetDateTime lastLoginAt;
    private String lastLoginIp;
    private Integer loginCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastModifiedAt;

    public UserQueryResult(String userId, String username, String email, UserStatus status,
                          OffsetDateTime lastLoginAt, String lastLoginIp, Integer loginCount,
                          OffsetDateTime createdAt, OffsetDateTime lastModifiedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.lastLoginIp = lastLoginIp;
        this.loginCount = loginCount;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
