package cn.treedeep.king.user.application.dto;

import cn.treedeep.king.user.domain.UserId;
import cn.treedeep.king.user.domain.UserStatus;
import org.springframework.stereotype.Component;

/**
 * 用户相关DTO转换器
 * <p>
 * 负责领域层对象与DTO之间的转换
 */
@Component
public class UserDtoConverter {

    /**
     * 将领域层用户状态转换为DTO
     */
    public UserStatusDto toDto(UserStatus userStatus) {
        if (userStatus == null) {
            return null;
        }
        return switch (userStatus) {
            case ACTIVE -> UserStatusDto.ACTIVE;
            case PENDING_ACTIVATION -> UserStatusDto.INACTIVE;
            case DISABLED -> UserStatusDto.LOCKED;
        };
    }

    /**
     * 将DTO用户状态转换为领域层对象
     */
    public UserStatus toDomain(UserStatusDto userStatusDto) {
        if (userStatusDto == null) {
            return null;
        }
        return switch (userStatusDto) {
            case ACTIVE -> UserStatus.ACTIVE;
            case INACTIVE -> UserStatus.PENDING_ACTIVATION;
            case LOCKED -> UserStatus.DISABLED;
        };
    }

    /**
     * 将领域层用户ID转换为DTO
     */
    public UserIdDto toDto(UserId userId) {
        if (userId == null) {
            return null;
        }
        return UserIdDto.of(userId.getValue());
    }

    /**
     * 将DTO用户ID转换为领域层对象
     */
    public UserId toDomain(UserIdDto userIdDto) {
        if (userIdDto == null) {
            return null;
        }
        return UserId.of(userIdDto.value());
    }
}
