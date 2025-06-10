package cn.treedeep.king.core.domain;

/**
 * 领域验证异常
 * <p>
 * 当领域规则或业务约束被违反时抛出此异常。<br>
 * 用于确保领域模型的完整性和一致性。
 * <p>
 * 验证场景：<br>
 * 1. 业务规则验证失败<br>
 * 2. 状态转换非法<br>
 * 3. 不变量被破坏<br>
 * 4. 值对象约束违反
 * <p>
 * 最佳实践：<br>
 * 1. 提供清晰的错误消息<br>
 * 2. 在领域模型中及早验证<br>
 * 3. 使用专门的验证方法<br>
 * 4. 区分技术验证和业务验证
 * <p>
 * 示例用法：
 * <pre>
 *     {@code
 * public class Order {
 *     public void addItem(OrderItem item) {
 *         if (this.status != OrderStatus.DRAFT) {
 *             throw new ValidationException("只能在草稿状态下添加商品");
 *         }
 *         if (items.size() >= 100) {
 *             throw new ValidationException("订单项不能超过100个");
 *         }
 *         items.add(item);
 *     }
 * }
 * }
 * </pre>
 */
public class ValidationException extends DomainException {

    private static final String ERROR_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(ERROR_CODE, message);
    }

    public ValidationException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
