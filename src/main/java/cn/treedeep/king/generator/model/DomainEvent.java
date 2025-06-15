package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领域事件信息
 * <p>
 * 表示系统中的业务事件，包含事件名称、关联的聚合根、事件数据等信息
 *
 * @author 周广明
 * @since 2025-01-10
 */
@Getter
public class DomainEvent {
    private final String name;
    private final String comment;
    private final String aggregateRootName;
    private final List<EventField> fields = new ArrayList<>();

    public DomainEvent(String name, String comment, String aggregateRootName) {
        this.name = name;
        this.comment = comment;
        this.aggregateRootName = aggregateRootName;
    }

    /**
     * 创建领域事件
     *
     * @param name 事件名称（如：UserRegistered）
     * @param comment 事件注释
     * @param aggregateRootName 关联的聚合根名称
     * @param fields 事件字段
     * @return DomainEvent实例
     */
    public static DomainEvent create(String name, String comment, String aggregateRootName, EventField... fields) {
        DomainEvent domainEvent = new DomainEvent(name, comment, aggregateRootName);
        domainEvent.fields.addAll(Arrays.stream(fields).toList());
        return domainEvent;
    }

    /**
     * 创建简单的领域事件（无关联聚合根）
     *
     * @param name 事件名称
     * @param comment 事件注释
     * @return DomainEvent实例
     */
    public static DomainEvent create(String name, String comment) {
        return new DomainEvent(name, comment, null);
    }

    /**
     * 设置关联的聚合根（链式调用）
     *
     * @param aggregateRootName 聚合根名称
     * @return DomainEvent实例，便于链式调用
     */
    public DomainEvent withAggregateRoot(String aggregateRootName) {
        DomainEvent newEvent = new DomainEvent(this.name, this.comment, aggregateRootName);
        newEvent.fields.addAll(this.fields);
        return newEvent;
    }

    /**
     * 设置表名（链式调用）
     *
     * @param tableName 表名
     * @return DomainEvent实例，便于链式调用
     */
    public DomainEvent withTableName(String tableName) {
        // 这里我们需要扩展类来支持tableName字段，但为了简化，暂时返回当前实例
        return this;
    }

    /**
     * 添加事件字段（链式调用）
     *
     * @param name 字段名
     * @param comment 字段注释
     * @param type 字段类型
     * @param columnName 列名
     * @return DomainEvent实例，便于链式调用
     */
    public DomainEvent addField(String name, String comment, String type, String columnName) {
        this.fields.add(EventField.create(name, comment, type, columnName));
        return this;
    }

    /**
     * 获取事件类名（首字母大写的名称）
     */
    public String getClassName() {
        return name;
    }

    /**
     * 获取事件表名（下划线分隔的小写名称）
     */
    public String getTableName() {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase() + "_events";
    }

    /**
     * 事件字段类
     */
    @Getter
    public static class EventField {
        private final String name;
        private final String type;
        private final String comment;
        private final String columnName;

        public EventField(String name, String type, String comment) {
            this.name = name;
            this.type = type;
            this.comment = comment;
            this.columnName = name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        }

        public EventField(String name, String type, String comment, String columnName) {
            this.name = name;
            this.type = type;
            this.comment = comment;
            this.columnName = columnName;
        }

        public static EventField create(String name, String type, String comment) {
            return new EventField(name, type, comment);
        }

        public static EventField create(String name, String type, String comment, String columnName) {
            return new EventField(name, type, comment, columnName);
        }
    }
}
