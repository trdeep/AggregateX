package cn.treedeep.king.core.domain.validation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证规则配置
 */
@Data
@ConfigurationProperties(prefix = "app.validation")
public class ValidationProperties {
    /**
     * 是否启用严格验证模式
     */
    private boolean strictMode = false;

    /**
     * 验证超时时间（毫秒）
     */
    private long validationTimeoutMs = 5000;

    /**
     * 是否允许空事件
     */
    private boolean allowEmptyEvents = false;

    /**
     * 每个聚合根的最大事件数量
     */
    private int maxEventsPerAggregate = 1000;

    /**
     * 业务规则配置
     */
    private Map<String, BusinessRuleConfig> businessRules = new HashMap<>();

    @Data
    public static class BusinessRuleConfig {
        /**
         * 规则是否启用
         */
        private boolean enabled = true;

        /**
         * 规则严重程度
         */
        private ValidationSeverity severity = ValidationSeverity.ERROR;

        /**
         * 自定义错误消息
         */
        private String errorMessage;
    }
}
