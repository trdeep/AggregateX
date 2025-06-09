package cn.treedeep.king.core.application.cqrs.query;

/**
 * 查询异常
 */
public class QueryException extends RuntimeException {

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
