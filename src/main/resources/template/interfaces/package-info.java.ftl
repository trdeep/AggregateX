/**
 * <h1>用户接口层 - Interfaces Layer</h1>
 * 系统与外部世界的交互边界，处理多样化的客户端请求和协议适配。
 *
 * <h5>核心职责：</h5>
 * <ul>
 *   <li>协议接入：处理 HTTP/RPC/消息队列等外部请求</li>
 *   <li>数据转换：将外部格式转换为内部 DTO 对象</li>
 *   <li>请求路由：将客户端请求分发给应用服务</li>
 *   <li>基础校验：执行身份认证和权限拦截</li>
 * </ul>
 *
 * <h5>关键组件：</h5>
 * <pre>
 *  ┌─────────────────┐
 *  │   Controller    │ - RESTful 接口端点
 *  │   MQListener    │ - 消息队列消费者
 *  │   ApiGateway    │ - 网关路由组件
 *  └─────────────────┘
 * </pre>
 *
 * <h5>设计规范：</h5>
 * <ol>
 *   <li>禁止包含业务逻辑，仅做协议适配和转换</li>
 *   <li>使用 DTO 隔离领域模型，保护核心业务</li>
 *   <li>异常统一处理，转换为客户端友好格式</li>
 *   <li>避免直接返回领域对象，防止数据泄露</li>
 * </ol>
 *
 * <h5>依赖关系：</h5>
 * <h6>
 *   → 调用 presentation 层进行数据转换<br>
 *   → 调用 application 层执行业务流程
 * </h6>
 *
 * @see ${packageName}.${moduleNameLower}.presentation 数据展现层
 * @see ${packageName}.${moduleNameLower}.application 应用服务层
 */
package ${packageName}.${moduleNameLower}.interfaces;
