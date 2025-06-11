package cn.treedeep.king.user.application.dto;

import cn.treedeep.king.user.domain.UserStatus;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 用户信息DTO
 */
@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private UserStatus status;
    private OffsetDateTime lastLoginAt;
    private String lastLoginIp;
    private Integer loginCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastModifiedAt;
}
