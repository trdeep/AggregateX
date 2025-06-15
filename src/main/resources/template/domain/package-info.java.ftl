/**
 * <h1>领域层 - Domain Layer</h1>
 * 系统核心，封装业务规则和领域模型。
 *
 * <h5>核心职责：</h5>
 * <ul>
 *   <li>业务建模：定义实体/值对象/聚合根</li>
 *   <li>规则实施：实现领域逻辑和业务约束</li>
 *   <li>状态管理：维护聚合内的数据一致性</li>
 *   <li>领域事件：定义业务事件模型</li>
 * </ul>
 *
 * <h5>关键组件：</h5>
 * <pre>
 *  ┌───────────────────┐
 *  │   AggregateRoot   │ - 聚合根（事务边界）
 *  │   Entity          │ - 具有身份标识的对象
 *  │   ValueObject     │ - 描述性不可变对象
 *  │   DomainService   │ - 跨聚合领域服务
 *  │   DomainEvent     │ - 业务事件定义
 *  │   Repository      │ - 持久化接口（契约）
 *  │   Factory         │ - 复杂对象创建逻辑
 *  └───────────────────┘
 * </pre>
 *
 * <h5>设计原则：</h5>
 * <ol>
 *   <li>富血模型：业务逻辑封装在领域对象中</li>
 *   <li>聚合内强一致性，聚合间最终一致</li>
 *   <li>技术零依赖：不包含框架注解</li>
 *   <li>接口定义仓储：解耦持久化细节</li>
 *   <li>领域服务处理跨聚合逻辑</li>
 * </ol>
 *
 * <h5>依赖关系：</h5>
 * <h6>
 *   ← 被 application 层调用<br>
 *   → 定义接口由 infrastructure 实现
 * </h6>
 *
 * @see ${packageName}.${moduleNameLower}.infrastructure 基础设施层
 */
package ${packageName}.${moduleNameLower}.domain;
