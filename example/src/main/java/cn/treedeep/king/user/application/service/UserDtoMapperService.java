package cn.treedeep.king.user.application.service;

import cn.treedeep.king.user.application.dto.UserDto;
import cn.treedeep.king.user.domain.User;

/**
 * 用户DTO转换器
 */
public class UserDtoMapperService {

    /**
     * 将User实体转换为UserDto
     */
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId().getValue());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail().getValue());
        dto.setStatus(user.getStatus());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setLastLoginIp(user.getLastLoginIp());
        dto.setLoginCount(user.getLoginCount());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastModifiedAt(user.getLastModifiedAt());

        return dto;
    }
}
