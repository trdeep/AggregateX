/**
 * <h1>领域层 - Domain Layer</h1>
 * 系统核心，封装业务规则和领域模型。
 *
 * <h2>领域服务 (Domain Service) 特性</h2>
 * <table border="1">
 *   <tr><th>维度</th><th>说明</th></tr>
 *   <tr>
 *     <td><b>职责</b></td>
 *     <td>
 *       - 实现跨聚合的业务规则<br>
 *       - 封装不适合放在实体中的逻辑<br>
 *       - 处理领域模型间的交互
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>业务逻辑</b></td>
 *     <td>包含核心业务规则（如：转账防欺诈规则）</td>
 *   </tr>
 *   <tr>
 *     <td><b>方法粒度</b></td>
 *     <td>单一职责操作（如：calculateTax）</td>
 *   </tr>
 *   <tr>
 *     <td><b>输入/输出</b></td>
 *     <td>接收领域对象，返回领域对象/基本类型</td>
 *   </tr>
 *   <tr>
 *     <td><b>典型操作</b></td>
 *     <td>
 *       <pre>{@code
 * public Money calculateTax(Order order) {
 *   // 业务规则：不同地区不同税率
 *   TaxRule rule = taxRuleRepository.findByRegion(order.getRegion());
 *   return order.getTotal().multiply(rule.getRate());
 * }}</pre>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>与其他层关系</b></td>
 *     <td><h6>
 *       ← 被 application 服务调用<br>
 *       ← 被领域实体调用<br>
 *       → 使用仓储接口访问持久化</h6>
 *     </td>
 *   </tr>
 * </table>
 *
 * <h5>设计原则：</h5>
 * <ol>
 *   <li>无状态：服务本身不维护业务状态</li>
 *   <li>显式命名：体现业务概念（如：FundsTransferService）</li>
 *   <li>避免贫血模型：优先将逻辑放在实体中</li>
 * </ol>
 *
 * @see ${packageName}.${moduleNameLower}.application 应用服务层
 * @see ${packageName}.${moduleNameLower}.infrastructure 基础设施层
 */
package ${packageName}.${moduleNameLower}.domain.service;
