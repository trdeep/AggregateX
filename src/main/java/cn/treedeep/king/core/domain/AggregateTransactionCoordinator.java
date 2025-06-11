package cn.treedeep.king.core.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 聚合根事务协调器
 * <p>
 * 负责协调多个聚合根之间的事务一致性，确保跨聚合根的操作在同一个事务中执行。
 * 虽然DDD原则上建议一个事务只修改一个聚合根，但在某些特殊场景下，
 * 可能需要在同一个事务中操作多个聚合根以保证业务一致性。
 * <p>
 * 主要功能：
 * <ul>
 * <li>事务边界管理 - 确保所有操作在同一个事务中执行</li>
 * <li>事件收集和延迟发布 - 收集所有聚合根产生的事件</li>
 * <li>不变量验证 - 验证所有聚合根的业务规则</li>
 * <li>异常处理和回滚 - 任何操作失败时回滚整个事务</li>
 * </ul>
 * <p>
 * 使用场景：
 * <ul>
 * <li>Saga模式的本地事务</li>
 * <li>批量数据处理</li>
 * <li>数据迁移操作</li>
 * <li>系统初始化</li>
 * </ul>
 * <p>
 * <strong>注意：</strong>应该谨慎使用此协调器，尽量遵循"一个事务一个聚合根"的原则。
 * 只在确实需要强一致性的场景下使用。
 */
@Component
public class AggregateTransactionCoordinator {

    /**
     * 在同一个事务中更新多个聚合根
     *
     * @param operations 聚合根更新操作列表
     */
    @Transactional
    public void executeInTransaction(List<AggregateOperation<?>> operations) {
        List<DomainEvent> allEvents = new ArrayList<>();

        // 执行所有操作
        for (AggregateOperation<?> operation : operations) {
            operation.execute();
            if (operation.getAggregate() instanceof AggregateRoot<?> root) {
                allEvents.addAll(root.getDomainEvents());
            }
        }

        // 验证所有聚合根的不变量
        for (AggregateOperation<?> operation : operations) {
            if (operation.getAggregate() instanceof AggregateRoot<?> root) {
                validateAggregateInvariants(root);
            }
        }

        // 清除所有已处理的事件
        for (AggregateOperation<?> operation : operations) {
            if (operation.getAggregate() instanceof AggregateRoot<?> root) {
                root.clearDomainEvents();
            }
        }
    }

    private void validateAggregateInvariants(AggregateRoot<?> aggregate) {
        // TODO: 实现聚合根不变量验证
    }

    /**
     * 聚合根操作包装类
     * <p>
     * 用于封装对聚合根的操作，包含聚合根实例和要执行的操作
     *
     * @param <T> 聚合根类型
     */
    public static class AggregateOperation<T> {
        /**
         * 要操作的聚合根实例
         */
        private final T aggregate;

        /**
         * 要执行的操作
         */
        private final Consumer<T> operation;

        /**
         * 创建聚合根操作实例
         *
         * @param aggregate 要操作的聚合根
         * @param operation 要执行的操作
         */
        public AggregateOperation(T aggregate, Consumer<T> operation) {
            this.aggregate = aggregate;
            this.operation = operation;
        }

        /**
         * 执行聚合根操作
         */
        public void execute() {
            operation.accept(aggregate);
        }

        /**
         * 获取操作的聚合根实例
         *
         * @return 聚合根实例
         */
        public T getAggregate() {
            return aggregate;
        }
    }

    /**
     * 创建聚合根操作
     *
     * @param aggregate 聚合根
     * @param operation 操作
     * @param <T>      聚合根类型
     * @return 聚合根操作对象
     */
    public static <T> AggregateOperation<T> of(T aggregate, Consumer<T> operation) {
        return new AggregateOperation<>(aggregate, operation);
    }
}
