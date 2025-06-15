/**
 * <h1>展现层 - Presentation Layer</h1>
 * 负责用户交互数据处理和视图模型转换，衔接接口层与业务层。
 *
 * <h5>核心职责：</h5>
 * <ul>
 *   <li>数据转换：将领域对象转换为视图模型(VO)</li>
 *   <li>会话管理：维护用户交互状态和上下文</li>
 *   <li>输入校验：执行基础数据格式验证</li>
 *   <li>响应组装：构建客户端所需的响应结构</li>
 * </ul>
 *
 * <h5>转换流程：</h5>
 * <pre>
 *  领域对象 → [Converter] → 视图模型(VO) → JSON/XML/HTML
 * </pre>
 *
 * <h5>关键组件：</h5>
 * <pre>
 *  ┌─────────────────┐
 *  │   DtoConverter  │ - DTO/VO 转换器
 *  │   ViewModel     │ - 视图模型对象
 *  │   SessionHolder │ - 用户会话管理器
 *  └─────────────────┘
 * </pre>
 *
 * <h5>设计规范：</h5>
 * <ol>
 *   <li>使用独立转换器处理复杂对象映射</li>
 *   <li>隔离 Web 框架技术（如 Spring MVC 注解）</li>
 *   <li>避免直接操作领域对象，通过应用服务协调</li>
 *   <li>视图模型应做数据脱敏处理</li>
 * </ol>
 *
 * <h5>依赖关系：</h5>
 * <h6>
 *   ← 接收 interfaces 层请求<br>
 *   → 调用 application 层获取业务数据
 * </h6>
 *
 * @see ${packageName}.${moduleNameLower}.interfaces 用户接口层
 * @see ${packageName}.${moduleNameLower}.application 应用服务层
 */
package ${packageName}.${moduleNameLower}.presentation;
