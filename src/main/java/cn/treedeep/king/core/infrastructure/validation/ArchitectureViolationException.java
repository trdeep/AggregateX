package cn.treedeep.king.core.infrastructure.validation;

/**
 * 架构违规异常
 * <p>
 * 当代码结构不符合DDD架构规范时抛出此异常
 */
public class ArchitectureViolationException extends RuntimeException {

    public ArchitectureViolationException(String message) {
        super(message);
    }

    public ArchitectureViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
