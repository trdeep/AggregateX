package cn.treedeep.king.core.domain.validation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证配置属性
 * <p>
 * 提供领域验证相关的配置属性，包括：
 * <ul>
 *   <li>严格模式配置</li>
 *   <li>验证超时配置</li>
 *   <li>业务规则配置</li>
 * </ul>
 */
@Data
@ConfigurationProperties(prefix = "app.validation")
public class ValidationProperties {
    /**
     * 是否启用严格模式
     * <p>
     * 在严格模式下，所有验证规则都会被强制执行，
     * 任何违反规则的情况都会导致验证失败。
     */
    private boolean strictMode = false;

    /**
     * 验证超时时间（毫秒）
     * <p>
     * 设置验证操作的最大执行时间，
     * 超过此时间将抛出验证超时异常。
     */
    private long timeout = 5000;

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
     * <p>
     * 存储业务规则相关的配置信息，
     * 键为规则名称，值为规则配置。
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
