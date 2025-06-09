package cn.treedeep.king.core.application.cqrs.query;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用查询条件
 */
@Data
public class QueryCriteria {
    private Map<String, Object> filters;
    private Map<String, String> sorts;

    public QueryCriteria() {
        this.filters = new HashMap<>();
        this.sorts = new HashMap<>();
    }

    /**
     * 添加过滤条件
     *
     * @param field 字段名
     * @param value 字段值
     * @return 当前对象
     */
    public QueryCriteria addFilter(String field, Object value) {
        filters.put(field, value);
        return this;
    }

    /**
     * 添加排序条件
     *
     * @param field 字段名
     * @param direction 排序方向 ("asc" 或 "desc")
     * @return 当前对象
     */
    public QueryCriteria addSort(String field, String direction) {
        sorts.put(field, direction);
        return this;
    }
}
