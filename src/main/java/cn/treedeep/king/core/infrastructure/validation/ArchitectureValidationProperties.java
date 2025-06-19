package cn.treedeep.king.core.infrastructure.validation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 架构验证配置属性类
 * <p>
 * 定义DDD架构验证的相关配置项，用于确保代码结构符合领域驱动设计的架构约束。
 * 基于ArchUnit框架实现编译期和运行期的架构合规性检查。
 * <p>
 * 验证规则：
 * <ul>
 * <li>分层架构约束 - 确保各层之间的依赖关系正确</li>
 * <li>聚合根完整性 - 验证聚合根的设计是否合规</li>
 * <li>仓储模式 - 检查仓储接口和实现的正确性</li>
 * <li>领域服务 - 验证领域服务的职责边界</li>
 * <li>事件发布 - 检查领域事件的正确使用</li>
 * </ul>
 * <p>
 * 配置示例：
 * <pre>
 *     {@code
 * app:
 *   architecture:
 *     validation:
 *       enabled: true
 *       fail-on-violation: true
 *       base-package: "com.example.domain"
 *       ignored-packages:
 *         - "..test.."
 *         - "..example.."
 *         }
 * </pre>
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.ddd.validation")
public class ArchitectureValidationProperties {

    /**
     * 是否启用架构校验
     */
    private boolean enabled = true;

    /**
     * 校验失败时是否中断应用启动
     */
    private boolean failOnViolation = true;

    /**
     * 是否启用详细日志
     */
    private boolean verboseLogging = true;

    /**
     * 日志是否显示忽略的类文件
     */
    private boolean logShowIgnoredClass = false;

    /**
     * 自定义包前缀，默认为项目包名
     */
    private String basePackage = "cn.treedeep.king";

    /**
     * 需要忽略的包模式
     * <p>
     * 模式匹配规则：<br>
     * ① .. 表示任意多个包层级<br>
     * ② * 表示单个包名中的任意字符<br>
     * ③ 精确包名匹配特定包路径
     */
    private String[] ignoredPackages = {
            "..test..",
            "..example..",
            "cn.treedeep.king.core.application.cqrs..",
            "cn.treedeep.king.core.domain..",
            "cn.treedeep.king.core.infrastructure.."
    };

    /**
     * 分层架构配置
     * <p>
     * 定义DDD分层架构中各层的包路径模式
     */
    private LayerConfig layers = new LayerConfig();

    /**
     * 分层架构配置类
     * <p>
     * 定义DDD分层架构中各层的包路径模式，用于架构验证
     */
    @Data
    public static class LayerConfig {
        /**
         * 领域层包模式
         */
        private final String domain = "..domain..";

        /**
         * 应用层包模式
         */
        private final String application = "..application..";

        /**
         * 基础设施层包模式
         */
        private final String infrastructure = "..infrastructure..";

        /**
         * 接口层包模式
         */
        private final String interfaces = "..interfaces..";

        /**
         * 表现层包模式
         */
        private final String presentation = "..presentation..";

        /**
         * 共享层包模式
         */
        public final String shared = "..shared..";

        /**
         * 防腐层包模式
         */
        private final String antiCorruption = "..acl..";
    }
}
