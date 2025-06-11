package cn.treedeep.king.core.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * 聚合根ID验证器实现
 * <p>
 * 用于验证聚合根ID的格式是否符合规范要求。
 * <p>
 * 验证规则：
 * <ul>
 * <li>不能为空或空白字符串</li>
 * <li>只允许包含字母、数字、下划线(_)和连字符(-)</li>
 * <li>必须符合正则表达式：^[a-zA-Z0-9_-]+$</li>
 * </ul>
 * <p>
 * 配合 {@link ValidAggregateId} 注解使用，提供声明式的ID格式验证。
 */
public class AggregateIdValidator implements ConstraintValidator<ValidAggregateId, String> {

    /**
     * 初始化验证器
     * <p>
     * 在验证开始前调用，用于初始化验证器的状态
     *
     * @param constraintAnnotation 约束注解实例
     */
    @Override
    public void initialize(ValidAggregateId constraintAnnotation) {
        // 初始化验证器
    }

    /**
     * 执行ID格式验证
     * <p>
     * 验证给定的ID字符串是否符合规范要求
     *
     * @param value 要验证的ID字符串
     * @param context 验证上下文
     * @return 如果ID格式有效返回true，否则返回false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        // ID规则验证：只允许字母、数字、下划线和连字符
        return value.matches("^[a-zA-Z0-9_-]+$");
    }
}
