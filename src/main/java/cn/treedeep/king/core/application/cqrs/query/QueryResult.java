package cn.treedeep.king.core.application.cqrs.query;

import lombok.Data;

@Data
public class QueryResult<T> {
    private Query<T> query;
    private T result;
    private long timestamp;

    public QueryResult(Query<T> query, T result) {
        this.query = query;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }
}
