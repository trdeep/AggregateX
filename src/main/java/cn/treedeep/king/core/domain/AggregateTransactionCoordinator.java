package cn.treedeep.king.core.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 聚合根事务协调器
 * <p>
 * 负责协调多个聚合根之间的事务一致性
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
     *
     * @param <T> 聚合根类型
     */
    public static class AggregateOperation<T> {
        private final T aggregate;
        private final Consumer<T> operation;

        public AggregateOperation(T aggregate, Consumer<T> operation) {
            this.aggregate = aggregate;
            this.operation = operation;
        }

        public void execute() {
            operation.accept(aggregate);
        }

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
