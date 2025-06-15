package cn.treedeep.king.shared.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用程序配置属性类
 * <p>
 * 定义AggregateX框架的全局配置项，提供应用级别的功能开关和参数配置。
 * 配置前缀为'app'，支持通过外部化配置进行定制。
 * <p>
 * 配置域：
 * <ul>
 * <li>审计配置 - 控制聚合根操作的审计日志记录</li>
 * <li>事件归档配置 - 控制历史事件的归档策略</li>
 * <li>监控配置 - 控制性能监控和指标收集</li>
 * <li>缓存配置 - 控制聚合根缓存行为</li>
 * </ul>
 * <p>
 * 配置示例：
 * <pre>{@code
 * app:
 *   audit:
 *     enabled: true
 *   event-archive:
 *     enabled: true
 *     }
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * 审计配置
     */
    private Audit audit = new Audit();

    /**
     * 事件归档配置
     */
    private EventArchive eventArchive = new EventArchive();

    /**
     * 事件归档配置类
     * <p>
     * 控制历史事件的归档策略和行为
     */
    @Getter
    @Setter
    public static class EventArchive {
        /**
         * 是否启用事件归档
         */
        private boolean enabled = false;
    }

    /**
     * 审计配置类
     * <p>
     * 控制聚合根操作的审计日志记录
     */
    @Getter
    @Setter
    public static class Audit {
        /**
         * 是否启用审计日志
         */
        private boolean enabled = false;
    }
}
