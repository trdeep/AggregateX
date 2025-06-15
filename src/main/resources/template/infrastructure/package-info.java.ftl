/**
 * <h1>基础设施层 - Infrastructure Layer</h1>
 * 技术实现细节，为各层提供技术支持。
 *
 * <h5>核心职责：</h5>
 * <ul>
 *   <li>持久化实现：数据库/文件系统操作</li>
 *   <li>消息通信：MQ/邮件/短信发送</li>
 *   <li>工具集成：缓存/定时任务/加密</li>
 *   <li>框架配置：技术组件初始化</li>
 * </ul>
 *
 * <h5>实现模式：</h5>
 * <pre>
 *     领域接口      →       技术实现
 *  ┌───────────┐       ┌──────────────┐
 *  │ Repository│ ◀───▶ │ JpaRepository│
 *  └───────────┘       └──────────────┘
 * </pre>
 *
 * <h5>关键组件：</h5>
 * <pre>
 *  ┌───────────────────┐
 *  │   Jpa/Hibernate   │ - ORM 实现
 *  │   RedisClient     │ - 缓存客户端
 *  │   KafkaProducer   │ - 消息生产者
 *  │   CloudStorage    │ - 云存储适配器
 *  │   ConfigLoader    │ - 配置加载器
 *  └───────────────────┘
 * </pre>
 *
 * <h5>设计规范：</h5>
 * <ol>
 *   <li>实现 domain 层定义的接口</li>
 *   <li>提供技术组件适配器</li>
 *   <li>处理跨系统通信细节</li>
 *   <li>隔离框架代码污染业务层</li>
 *   <li>为各层提供通用技术能力</li>
 * </ol>
 *
 * @see ${packageName}.${moduleNameLower}.domain 领域模型层
 */
package ${packageName}.${moduleNameLower}.infrastructure;
