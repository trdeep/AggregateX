package cn.treedeep.king.core.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * 聚合根ID验证器实现
 */
public class AggregateIdValidator implements ConstraintValidator<ValidAggregateId, String> {

    @Override
    public void initialize(ValidAggregateId constraintAnnotation) {
        // 初始化验证器
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        // ID规则验证：只允许字母、数字、下划线和连字符
        return value.matches("^[a-zA-Z0-9_-]+$");
    }
}
