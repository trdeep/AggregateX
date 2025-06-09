package cn.treedeep.king.core.domain;

/**
 * 并发冲突异常
 * <p>
 * 在以下情况下抛出:<br>
 * 1. 乐观锁冲突<br>
 * 2. 版本号不匹配<br>
 * 3. 并发修改导致的状态不一致
 */
public class ConcurrencyConflictException extends DomainException {

    private static final String ERROR_CODE = "CONCURRENCY_CONFLICT";
    private static final String DOMAIN_NAME = "AGGREGATE_ROOT";

    public ConcurrencyConflictException(String message) {
        super(message, ERROR_CODE, DOMAIN_NAME);
    }

    public ConcurrencyConflictException(String message, Throwable cause) {
        super(message, ERROR_CODE, DOMAIN_NAME, cause);
    }
}
