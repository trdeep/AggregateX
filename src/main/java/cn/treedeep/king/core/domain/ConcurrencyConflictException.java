package cn.treedeep.king.core.domain;

/**
 * 并发冲突异常
 * <p>
 * 当多个操作同时修改同一个聚合根时发生的并发冲突异常。
 * 这是领域层的一个重要异常，用于处理在分布式或多线程环境下的数据一致性问题。
 * <p>
 * 常见触发场景：
 * <ul>
 * <li>乐观锁冲突 - 基于版本号的并发控制失败</li>
 * <li>版本号不匹配 - 聚合根版本与期望版本不一致</li>
 * <li>并发修改 - 多个事务同时修改同一聚合根</li>
 * <li>状态不一致 - 聚合根状态发生了意外变化</li>
 * </ul>
 * <p>
 * 处理策略：
 * <ul>
 * <li>重试机制 - 自动重新加载聚合根并重试操作</li>
 * <li>用户提示 - 告知用户数据已被其他用户修改</li>
 * <li>合并策略 - 尝试智能合并冲突的修改</li>
 * <li>版本回退 - 回退到上一个稳定版本</li>
 * </ul>
 * <p>
 * 最佳实践：
 * <ul>
 * <li>使用指数退避算法进行重试</li>
 * <li>限制重试次数避免无限循环</li>
 * <li>记录冲突详情便于问题分析</li>
 * <li>在应用层转换为用户友好的错误信息</li>
 * </ul>
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
