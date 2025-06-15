/**
 * <h1>应用层 - Application Layer</h1>
 * 业务流程协调者，负责用例编排和事务管理。
 *
 * <h5>核心职责：</h5>
 * <ul>
 *   <li>事务控制：声明式事务边界管理</li>
 *   <li>流程编排：协调领域对象完成业务目标</li>
 *   <li>跨域协调：调度多个聚合协同工作</li>
 *   <li>事件发布：触发领域事件通知</li>
 * </ul>
 *
 * <h5>关键组件：</h5>
 * <pre>
 *  ┌─────────────────────┐
 *  │ ApplicationService  │ - 应用服务（用例入口）
 *  │ Command/Query       │ - CQRS 模式指令对象
 *  │ EventPublisher      │ - 领域事件发布器
 *  └─────────────────────┘
 * </pre>
 *
 * <h5>设计规范：</h5>
 * <ol>
 *   <li>方法命名体现业务意图（如：transferFunds）</li>
 *   <li>单个方法对应完整业务用例</li>
 *   <li>禁止包含领域规则，仅做流程控制</li>
 *   <li>返回 DTO 而非领域对象</li>
 *   <li>异常处理：捕获领域异常并转换</li>
 * </ol>
 *
 * <h5>依赖关系：</h5>
 * <h6>
 *   ← 接收 interfaces、presentation 层请求<br>
 *   → 调用 domain 层领域能力<br>
 *   → 委托 infrastructure 层技术实现
 * </h6>
 *
 * @see ${packageName}.${moduleNameLower}.domain 领域模型层
 * @see ${packageName}.${moduleNameLower}.infrastructure 基础设施层
 */
package ${packageName}.${moduleNameLower}.application;
