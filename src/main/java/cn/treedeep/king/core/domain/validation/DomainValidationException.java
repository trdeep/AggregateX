package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.DomainException;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

/**
 * 增强的领域验证异常
 */
@Getter
public class DomainValidationException extends DomainException {
    private final String violatedRule;
    private final Map<String, Object> context;
    private final ValidationSeverity severity;

    /**
     * 创建领域验证异常
     *
     * @param rule     违反的规则
     * @param message  错误信息
     * @param context  验证上下文
     * @param severity 严重程度
     */
    public DomainValidationException(String rule, String message,
                                   Map<String, Object> context,
                                   ValidationSeverity severity) {
        super("VALIDATION_ERROR", message);
        this.violatedRule = rule;
        this.context = Collections.unmodifiableMap(context);
        this.severity = severity;
    }

    /**
     * 简化的构造函数，适用于简单验证错误
     */
    public DomainValidationException(String rule, String message) {
        this(rule, message, Collections.emptyMap(), ValidationSeverity.ERROR);
    }
}
