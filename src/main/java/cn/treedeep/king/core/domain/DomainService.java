package cn.treedeep.king.core.domain;

/**
 * 领域服务接口
 * <p>
 * 用于封装那些不自然属于任何实体或值对象的领域逻辑。
 * <p>
 * 典型应用场景：<br>
 * 1. 需要协调多个聚合根的操作<br>
 * 2. 复杂的业务规则校验<br>
 * 3. 涉及外部资源或服务的领域操作
 * <p>
 * 领域服务设计原则：<br>
 * 1. 保持无状态<br>
 * 2. 只包含领域逻辑，不包含技术实现细节<br>
 * 3. 方法名应该体现领域概念和行为<br>
 * 4. 参数和返回值应该是领域对象
 * <p>
 * 实现约束：<br>
 * 1. 不允许直接依赖基础设施层组件<br>
 * 2. 不允许包含业务流程编排逻辑<br>
 * 3. 不允许直接处理事务边界<br>
 * 4. 验证逻辑应该委托给专门的验证器
 */
public interface DomainService {

    /**
     * 获取服务名称，用于日志和监控
     */
    default String getServiceName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 验证服务操作的前置条件
     *
     * @param operation 操作名称
     * @throws IllegalStateException 如果前置条件不满足
     */
    default void validatePrecondition(String operation) {
        // 由具体服务实现类重写
    }

    /**
     * 验证服务操作的后置条件
     *
     * @param operation 操作名称
     * @throws IllegalStateException 如果后置条件不满足
     */
    default void validatePostcondition(String operation) {
        // 由具体服务实现类重写
    }
}
