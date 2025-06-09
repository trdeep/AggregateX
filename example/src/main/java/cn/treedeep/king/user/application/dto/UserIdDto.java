package cn.treedeep.king.user.application.dto;

/**
 * 用户ID DTO
 * <p>
 * 用于接口层和应用层之间的数据传输，避免接口层直接依赖领域层的UserId值对象
 */
public record UserIdDto(String value) {
    public static UserIdDto of(String value) {
        return new UserIdDto(value);
    }
}
