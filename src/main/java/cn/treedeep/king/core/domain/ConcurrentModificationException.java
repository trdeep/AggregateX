package cn.treedeep.king.core.domain;

/**
 * 并发修改异常
 * <p>
 * 当尝试保存聚合根时，如果发现版本号不匹配，则抛出此异常。<br>
 * 这表示在当前操作进行期间，其他操作已经修改了该聚合根。
 * <p>
 * 触发场景：<br>
 * 1. 多个用户同时修改同一个聚合根<br>
 * 2. 乐观锁检测到版本不匹配<br>
 * 3. 事件溯源中事件版本不连续
 * <p>
 * 处理建议：<br>
 * 1. 在应用层实现重试机制<br>
 * 2. 向用户显示冲突提示<br>
 * 3. 提供合并或放弃更改的选项<br>
 * 4. 记录日志以便分析并发问题
 * <p>
 * 示例处理：
 * <pre>
 * {@code
 * try {
 *     orderService.updateOrder(orderId, updateCommand);
 * } catch (ConcurrentModificationException e) {
 *     // 提示用户刷新后重试
 *     throw new ApplicationException("订单已被他人修改，请刷新后重试");
 * }
 * }
 * </pre>
 */
public class ConcurrentModificationException extends DomainException {

    private static final String ERROR_CODE = "CONCURRENT_MODIFICATION";

    /**
     * 构造并发修改异常
     *
     * @param aggregateId 聚合根ID
     * @param expectedVersion 预期的版本号
     * @param actualVersion 实际的版本号
     */
    public ConcurrentModificationException(String aggregateId, long expectedVersion, long actualVersion) {
        super(ERROR_CODE,
                String.format("Concurrent modification detected for aggregate '%s'. Expected version: %d, actual version: %d",
                        aggregateId, expectedVersion, actualVersion));
    }
}
