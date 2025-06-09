package cn.treedeep.king.user.application.dto;

/**
 * 用户状态DTO枚举
 * <p>
 * 用于接口层和应用层之间的数据传输，避免接口层直接依赖领域层
 */
public enum UserStatusDto {
    /**
     * 激活状态
     */
    ACTIVE,

    /**
     * 非激活状态
     */
    INACTIVE,

    /**
     * 锁定状态
     */
    LOCKED
}
