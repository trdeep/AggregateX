package cn.treedeep.king.core.domain.cqrs;

import java.io.Serializable;

/**
 * 查询基类
 * <p>
 * 所有查询对象都应该继承此类，查询代表系统中的读操作。
 * Domain层的核心查询定义。
 */
public abstract class Query implements Serializable {
    private final String queryId;
    private final long timestamp;

    protected Query() {
        this.queryId = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public String getQueryId() {
        return queryId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取查询结果类型
     */
    public abstract Class<?> getResultType();

    /**
     * 获取查询名称
     */
    public abstract String getQueryName();
}
