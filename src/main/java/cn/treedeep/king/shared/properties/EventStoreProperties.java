package cn.treedeep.king.shared.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 事件存储配置属性
 */
@Data
@ConfigurationProperties(prefix = "app.event-store")
public class EventStoreProperties {
    /**
     * 事件存储类型：memory（内存存储）或 jpa（数据库存储）
     */
    private String type = "jpa";

    /**
     * 事件表名称（JPA模式）
     */
    private String tableName = "events";

    /**
     * 批量操作大小
     */
    private int batchSize = 1000;

    /**
     * 快照配置
     */
    @NestedConfigurationProperty
    private EventStoreSnapshotProperties snapshot = new EventStoreSnapshotProperties();


    /**
     * 事件存储快照配置属性
     */
    @Data
    public static class EventStoreSnapshotProperties {
        /**
         * 是否启用快照功能
         */
        private boolean enabled = false;

        /**
         * 快照生成频率（事件数）
         */
        private int frequency = 100;
    }

}
