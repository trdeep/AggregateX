/**
 * <h1>基础设施层 - Infrastructure Layer</h1>
 * 技术实现细节，为各层提供技术支持。
 *
 * <h2>基础设施服务 (Infrastructure Service) 特性</h2>
 * <table border="1">
 *   <tr><th>维度</th><th>说明</th></tr>
 *   <tr>
 *     <td><b>职责</b></td>
 *     <td>
 *       - 技术细节实现<br>
 *       - 第三方系统集成<br>
 *       - 框架特定逻辑
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>业务逻辑</b></td>
 *     <td>不包含业务规则，纯技术实现</td>
 *   </tr>
 *   <tr>
 *     <td><b>方法粒度</b></td>
 *     <td>技术操作单元（如：sendSMS）</td>
 *   </tr>
 *   <tr>
 *     <td><b>输入/输出</b></td>
 *     <td>技术相关参数（如：消息队列的Message对象）</td>
 *   </tr>
 *   <tr>
 *     <td><b>典型操作</b></td>
 *     <td>
 *       <pre>{@code
 * @Override
 * public void publish(DomainEvent event) {
 *   // 技术实现：Kafka消息发送
 *   kafkaTemplate.send("domain_events", event.toJson());
 * }}</pre>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td><b>与其他层关系</b></td>
 *     <td><h6>
 *       ← 实现 domain 层定义的接口<br>
 *       ← 被 application 层直接调用<br>
 *       ← 被其他基础设施组件使用</h6>
 *     </td>
 *   </tr>
 * </table>
 *
 * <h5>服务类型：</h5>
 * <ul>
 *   <li><h5>适配器服务：</h5>实现领域接口（如：SmsServiceAdapter）</li>
 *   <li><h5>技术工具服务：</h5>提供通用技术能力（如：EncryptionService）</li>
 *   <li><h5>第三方集成服务：</h5>封装外部系统调用（如：PaymentGatewayService）</li>
 * </ul>
 *
 * @see ${packageName}.${moduleNameLower}.domain 领域模型层
 * @see ${packageName}.${moduleNameLower}.application 应用服务层
 */
package ${packageName}.${moduleNameLower}.infrastructure.service;
