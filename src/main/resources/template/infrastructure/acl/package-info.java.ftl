/**
 * 防腐层（Anti-Corrosion Layer, ACL）详解
 *
 * <p>防腐层（Anti-Corruption Layer，ACL）是领域驱动设计（DDD）中的一种重要模式，用于隔离不同限界上下文或外部系统，
 * 防止外部模型的"腐蚀"影响到核心领域模型。
 *
 * <h2>为什么需要防腐层？</h2>
 * <ol>
 *   <li><strong>模型隔离：</strong>防止外部系统的领域模型污染内部核心模型</li>
 *   <li><strong>变化缓冲：</strong>外部系统变更时只需修改防腐层，不影响核心业务逻辑</li>
 *   <li><strong>协议转换：</strong>将外部系统的数据格式/协议转换为内部标准</li>
 *   <li><strong>异常处理：</strong>统一处理外部系统调用产生的各种异常</li>
 * </ol>
 *
 * <h2>防腐层的典型应用场景</h2>
 * <ol>
 *   <li>集成遗留系统</li>
 *   <li>调用第三方服务（如支付、短信、地图等）</li>
 *   <li>微服务架构中服务间调用</li>
 *   <li>不同限界上下文之间的交互</li>
 * </ol>
 *
 * <h2>防腐层的实现方式</h2>
 *
 * <h3>1. 适配器模式（最常用）</h3>
 * <pre>
 * {@code
 * // 领域层接口定义
 * public interface ExternalPaymentService {
 *     PaymentResult process(PaymentCommand command);
 * }
 * // 基础设施层实现（防腐层）
 * public class PayPalAdapter implements ExternalPaymentService {
 *     private final PayPalClient payPalClient;
 *     private final PaymentResponseMapper mapper;
 *
 *     public PaymentResult process(PaymentCommand command) {
 *         PayPalRequest request = mapper.toRequest(command);
 *         PayPalResponse response = payPalClient.createPayment(request);
 *         return mapper.toDomain(response);
 *     }
 * }
 * }
 * </pre>
 * <h3>2. 外观模式</h3>
 * <pre>
 * {@code
 * public class ShippingServiceFacade {
 *     private final FedexClient fedexClient;
 *     private final DhlClient dhlClient;
 *
 *     public TrackingInfo getTrackingInfo(String orderId) {
 *         // 统一多个物流系统的接口差异
 *         try {
 *             return fedexClient.getTracking(orderId);
 *         } catch (Exception e) {
 *             return dhlClient.queryTracking(orderId);
 *         }
 *     }
 * }
 * }
 * </pre>
 * <h3>3. 网关模式</h3>
 * <pre>
 * {@code
 * public class InventoryServiceGateway {
 *     private final InventoryRestClient restClient;
 *     private final InventoryCache cache;
 *
 *     public boolean checkStock(ProductId productId, int quantity) {
 *         // 添加缓存层
 *         return cache.get(productId,
 *             () -> restClient.checkStock(productId, quantity));
 *     }
 * }
 * }
 * </pre>
 * <h2>防腐层的核心组件</h2>
 *
 * <h3>1. 转换器（Mapper）</h3>
 * <pre>
 * {@code
 * public class PaymentResponseMapper {
 *     public PaymentResult toDomain(PayPalResponse response) {
 *         return new PaymentResult(
 *             new PaymentId(response.getTransactionId()),
 *             mapStatus(response.getStatus()),
 *             response.getProcessedAt()
 *         );
 *     }
 * }
 * }
 * </pre>
 * <h3>2. 异常转换器</h3>
 * <pre>
 * {@code
 * try {
 *     return externalService.call();
 * } catch (ExternalServiceException e) {
 *     throw new DomainOperationException("业务操作失败", e);
 * }
 * }
 * </pre>
 * <h3>3. DTO对象</h3>
 * <pre>
 * {@code
 * public class ExternalUserDTO {
 *     private String externalId;
 *     private String name;
 *     private String email;
 *     // 外部系统特有的字段
 *     private String externalType;
 * }
 * }
 * </pre>
 * <h2>防腐层的最佳实践</h2>
 * <ol>
 *   <li><strong>明确边界：</strong>
 *     <ul>
 *       <li>防腐层应位于基础设施层</li>
 *       <li>领域层只依赖防腐层接口，不依赖实现</li>
 *     </ul>
 *   </li>
 *   <li><strong>单一职责：</strong>
 *     <ul>
 *       <li>每个防腐类只处理一个外部系统的适配</li>
 *     </ul>
 *   </li>
 *   <li><strong>防御式编程：</strong>
 *   <pre>
 *     {@code
 *     public UserInfo getUser(String userId) {
 *         ExternalUserDTO dto = externalService.getUser(userId);
 *         if (dto == null || dto.getName() == null) {
 *             throw new CorruptedDataException("无效的用户数据");
 *         }
 *         return mapper.toDomain(dto);
 *     }
 *     }
 *     </pre>
 *   </li>
 *   <li><strong>性能考虑：</strong>
 *     <ul>
 *       <li>添加缓存减少外部调用</li>
 *       <li>考虑异步调用方式</li>
 *     </ul>
 *   </li>
 *   <li><strong>测试策略：</strong>
 *     <ul>
 *       <li>防腐层需要单独的集成测试</li>
 *       <li>核心业务逻辑可mock防腐层进行测试</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * <h2>防腐层在架构中的位置</h2>
 * <pre>
 * ┌───────────────────────┐
 * │       应用层           │
 * └──────────┬────────────┘
 *            │
 * ┌──────────▼────────────┐
 * │       领域层          │  ← 定义防腐层接口
 * └──────────┬────────────┘
 *            │
 * ┌──────────▼────────────┐
 * │     基础设施层        │  ← 实现防腐层
 * └──────────┬────────────┘
 *            │
 * ┌──────────▼────────────┐
 * │      外部系统         │
 * └───────────────────────┘
 * </pre>
 *
 * <h2>实际案例：电商系统支付集成</h2>
 * <pre>
 * {@code
 * // 领域层接口
 * public interface PaymentGateway {
 *     PaymentResult process(Payment payment);
 *     PaymentStatus queryStatus(PaymentId id);
 * }
 *
 * // 防腐层实现（基础设施层）
 * public class StripePaymentAdapter implements PaymentGateway {
 *     private final StripeClient stripe;
 *     private final PaymentMapper mapper;
 *
 *     public PaymentResult process(Payment payment) {
 *         StripeChargeRequest request = mapper.toRequest(payment);
 *         StripeChargeResponse response = stripe.charge(request);
 *         return mapper.toDomain(response);
 *     }
 *
 *     public PaymentStatus queryStatus(PaymentId id) {
 *         StripeChargeStatus status = stripe.getChargeStatus(id.getValue());
 *         return mapper.toStatusDomain(status);
 *     }
 * }
 *
 * // 应用服务使用
 * public class OrderService {
 *     private final PaymentGateway paymentGateway;
 *
 *     public void completeOrder(Order order) {
 *         PaymentResult result = paymentGateway.process(order.getPayment());
 *         order.confirmPayment(result);
 *     }
 * }
 * }
 * </pre>
 * <h2>防腐层的优势总结</h2>
 * <ol>
 *   <li><strong>保护领域模型：</strong>防止外部模型概念渗入核心领域</li>
 *   <li><strong>降低耦合度：</strong>外部系统变更的影响范围可控</li>
 *   <li><strong>提高可测试性：</strong>核心业务逻辑可不依赖真实外部系统进行测试</li>
 *   <li><strong>统一异常处理：</strong>集中处理各种外部系统异常</li>
 *   <li><strong>便于替换：</strong>更换外部系统时只需修改防腐层实现</li>
 * </ol>
 *
 * <h2>何时不需要防腐层？</h2>
 * <ol>
 *   <li>集成的外部系统非常简单且稳定</li>
 *   <li>外部模型与内部模型高度一致</li>
 *   <li>临时性的简单集成（但需评估未来是否可能复杂化）</li>
 * </ol>
 *
 * <p>对于大多数企业级应用，特别是需要与多个外部系统集成的场景，实现良好的防腐层能显著提高系统的可维护性和演进能力。
 */
package ${packageName}.${moduleNameLower}.infrastructure.acl;
