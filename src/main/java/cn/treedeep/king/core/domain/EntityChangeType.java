package cn.treedeep.king.core.domain;

/**
 * 实体变更类型枚举
 */
public enum EntityChangeType {
    /**
     * 新增
     */
    ADDED,

    /**
     * 修改
     */
    MODIFIED,

    /**
     * 删除
     */
    DELETED,

    /**
     * 未变更
     */
    UNCHANGED
}
