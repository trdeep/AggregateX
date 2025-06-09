package cn.treedeep.king.core.domain;

/**
 * 聚合根未找到异常
 * <p>
 * 当尝试加载一个不存在的聚合根时抛出此异常。
 * <p>
 * 这通常发生在以下场景：<br>
 * 1. 通过ID查询不存在的聚合根<br>
 * 2. 处理命令时目标聚合根不存在<br>
 * 3. 尝试加载已被删除的聚合根
 * <p>
 * 异常处理建议：<br>
 * 1. 在应用层捕获并转换为适当的响应<br>
 * 2. 对于REST API，通常映射为404 Not Found<br>
 * 3. 在查询时可以返回Optional来避免异常
 * <p>
 * 示例用法：
 * <pre>
 * public Order getOrder(OrderId orderId) {
 *     return repository.findById(orderId)
 *         .orElseThrow(() -> new AggregateNotFoundException(orderId.toString()));
 * }
 * </pre>
 */
public class AggregateNotFoundException extends DomainException {

    private static final String ERROR_CODE = "AGGREGATE_NOT_FOUND";

    public AggregateNotFoundException(String aggregateId) {
        super(ERROR_CODE, String.format("Aggregate with id '%s' not found", aggregateId));
    }
}
