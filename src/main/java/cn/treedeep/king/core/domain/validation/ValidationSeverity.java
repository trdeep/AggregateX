package cn.treedeep.king.core.domain.validation;

import lombok.Getter;

/**
 * 验证严重程度枚举
 * <p>
 * 定义验证结果的严重程度级别，用于：
 * <ul>
 *   <li>区分验证问题的严重性</li>
 *   <li>控制验证失败的处理方式</li>
 *   <li>提供验证结果的分类</li>
 * </ul>
 */
public enum ValidationSeverity {

    /**
     * 信息级别
     * <p>
     * 表示验证发现的问题仅作为提示信息，
     * 不影响业务操作的继续执行。
     */
    INFO("提示"),

    /**
     * 警告级别
     * <p>
     * 表示验证发现的问题需要关注，
     * 但不会阻止业务操作的执行。
     */
    WARNING("警告"),

    /**
     * 错误级别
     * <p>
     * 表示验证发现的问题需要立即处理，
     * 将阻止业务操作的执行。
     */
    ERROR("错误"),

    /**
     * 严重级别
     * <p>
     * 表示验证发现的问题非常严重，
     * 需要立即处理并可能触发告警。
     */
    CRITICAL("严重错误");

    @Getter
    private final String description;

    ValidationSeverity(String description) {
        this.description = description;
    }
}
