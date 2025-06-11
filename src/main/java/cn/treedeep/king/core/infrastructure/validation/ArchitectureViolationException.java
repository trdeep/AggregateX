package cn.treedeep.king.core.infrastructure.validation;

/**
 * 架构违规异常
 * <p>
 * 当代码结构不符合DDD架构规范时抛出此异常
 */
public class ArchitectureViolationException extends RuntimeException {

    /**
     * 创建一个架构违规异常实例
     *
     * @param message 异常消息
     */
    public ArchitectureViolationException(String message) {
        super(message);
    }

    /**
     * 创建一个带有原因的架构违规异常实例
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ArchitectureViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
