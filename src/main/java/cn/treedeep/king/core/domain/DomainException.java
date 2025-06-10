package cn.treedeep.king.core.domain;

import lombok.Getter;

/**
 * 领域异常基类
 * <p>
 * 所有的领域异常都应该继承此类。用于表示领域规则验证失败、
 * 业务约束违反等领域相关的异常情况。
 * <p>
 * 设计特点：<br>
 * 1. 继承自RuntimeException，无需显式捕获<br>
 * 2. 包含错误码，便于客户端处理<br>
 * 3. 支持嵌套异常，保留完整异常链<br>
 * 4. 提供清晰的错误消息
 * <p>
 * 使用规范：<br>
 * 1. 每种领域错误应定义独特的错误码<br>
 * 2. 错误消息应该清晰描述问题<br>
 * 3. 在领域层抛出，在应用层统一处理<br>
 * 4. 避免在领域层捕获此类异常
 * <p>
 * 示例：
 * <pre>
 *     {@code
 * public class InsufficientBalanceException extends DomainException {
 *     public InsufficientBalanceException(String accountId, Money balance, Money required) {
 *         super("INSUFFICIENT_BALANCE",
 *               String.format("账户 %s 余额不足。当前余额: %s, 需要: %s",
 *                           accountId, balance, required));
 *     }
 * }
 * }
 * </pre>
 */
@Getter
public class DomainException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 领域名称
     */
    private String domainName;

    /**
     * 创建领域异常
     *
     * @param errorCode 错误码
     * @param message   错误消息
     */
    protected DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 创建领域异常
     *
     * @param message   错误消息
     * @param errorCode 错误码
     * @param cause     原始异常
     */
    protected DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }


    /**
     * 创建领域异常
     *
     * @param message    错误消息
     * @param errorCode  错误码
     * @param domainName 领域名称
     */
    protected DomainException(String message, String errorCode, String domainName) {
        super(message);
        this.errorCode = errorCode;
        this.domainName = domainName;
    }

    /**
     * 创建领域异常
     *
     * @param message    错误消息
     * @param errorCode  错误码
     * @param domainName 领域名称
     * @param cause      原始异常
     */
    protected DomainException(String message, String errorCode, String domainName, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.domainName = domainName;
    }


}
