/**
 * <h1>应用层 - Application Layer</h1>
 * 业务流程协调者，负责用例编排和事务管理。
 *
 * <h2>应用服务 (Application Service) 特性</h2>
 * <table border="1">
 *   <tr><th>维度</th><th>说明</th></tr>
 *   <tr>
 *     <td><b>职责</b></td>
 *     <td>
 *       - 业务流程编排（工作流控制）<br>
 *       - 事务边界管理<br>
 *       - 跨聚合协调<br>
 *       - 领域事件发布
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>业务逻辑</b></td>
 *     <td>不包含具体业务规则，仅协调领域对象</td>
 *   </tr>
 *   <tr>
 *     <td><b>方法粒度</b></td>
 *     <td>每个方法对应完整业务用例（如：placeOrder）</td>
 *   </tr>
 *   <tr>
 *     <td><b>输入/输出</b></td>
 *     <td>接收 Command/DTO，返回 DTO</td>
 *   </tr>
 *   <tr>
 *     <td><b>典型操作</b></td>
 *     <td>
 *       <pre>
    {@code
 * @Transactional
 * public OrderResult placeOrder(OrderCommand cmd) {
 *   // 1. 校验库存（调用领域服务）
 *   // 2. 创建订单聚合
 *   // 3. 支付处理（跨聚合协调）
 *   // 4. 发布OrderPlacedEvent
 * }}</pre>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>与其他层关系</b></td>
 *     <td>
 *       ← 被 presentation 层调用<br>
 *       → 调用 domain 层能力<br>
 *       → 委托 infrastructure 技术实现
 *     </td>
 *   </tr>
 * </table>
 *
 * <h5>对比领域服务：</h5>
 * <ul>
 *   <li>应用服务：<i>协调多个领域对象</i>完成业务目标</li>
 *   <li>领域服务：实现<i>单个跨聚合</i>的业务规则</li>
 * </ul>
 *
 * @see ${packageName}.${moduleNameLower}.domain 领域模型层
 * @see ${packageName}.${moduleNameLower}.infrastructure 基础设施层
 */
package ${packageName}.${moduleNameLower}.application.service;
