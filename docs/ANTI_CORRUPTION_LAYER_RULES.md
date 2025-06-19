# 防腐层（Anti-Corruption Layer）架构验证规则

## 概述

防腐层（ACL）是DDD中的一个重要模式，用于隔离外部系统对核心领域模型的影响。本文档描述了架构验证器中关于防腐层的验证规则。

## 防腐层位置

防腐层应该位于基础设施层的 `acl` 包中：
```
infrastructure/
  └── acl/
      ├── adapter/      # 适配器实现
      ├── gateway/      # 外部网关实现  
      ├── translator/   # 模型转换器
      └── client/       # 外部系统客户端
```

## 验证规则

### 1. 层级约束
- ✅ 防腐层只能访问领域层和共享层
- ✅ 应用层可以访问防腐层
- ❌ 防腐层不能直接依赖应用层、表现层、接口层

### 2. 命名约定
- 适配器类必须以 `Adapter` 结尾
- 网关类必须以 `Gateway` 结尾
- 转换器类必须以 `Translator` 或 `Converter` 结尾
- 外观类必须以 `Facade` 结尾

### 3. 接口定义原则
- 防腐层接口应该定义在领域层
- 防腐层实现应该在基础设施层的 `acl` 包中

### 4. 依赖隔离
- ❌ 防腐层不应该将外部模型暴露给领域层
- ❌ 防腐层不应该让外部异常泄露到领域层
- ✅ 所有外部依赖应该通过防腐层进行隔离

### 5. Spring 注解规范
- 防腐层实现类应该使用 `@Component` 或 `@Service` 注解
- 防腐层配置类应该使用 `@Configuration` 注解

## 典型实现示例

### 外部支付系统防腐层

**领域层接口定义**：
```java
// domain/service/PaymentGateway.java
public interface PaymentGateway {
    PaymentResult processPayment(Payment payment);
}
```

**防腐层实现**：
```java
// infrastructure/acl/adapter/PaymentGatewayAdapter.java
@Component
public class PaymentGatewayAdapter implements PaymentGateway {
    
    private final ExternalPaymentClient client;
    private final PaymentTranslator translator;
    
    @Override
    public PaymentResult processPayment(Payment payment) {
        try {
            // 转换领域模型到外部模型
            ExternalPaymentRequest request = translator.toExternal(payment);
            
            // 调用外部系统
            ExternalPaymentResponse response = client.processPayment(request);
            
            // 转换外部模型到领域模型
            return translator.toDomain(response);
            
        } catch (ExternalPaymentException e) {
            // 转换外部异常到领域异常
            throw new PaymentProcessingException("支付处理失败", e);
        }
    }
}
```

**模型转换器**：
```java
// infrastructure/acl/translator/PaymentTranslator.java
@Component
public class PaymentTranslator {
    
    public ExternalPaymentRequest toExternal(Payment payment) {
        // 领域模型 -> 外部模型转换逻辑
    }
    
    public PaymentResult toDomain(ExternalPaymentResponse response) {
        // 外部模型 -> 领域模型转换逻辑
    }
}
```

## 配置示例

在 `application.yml` 中启用防腐层验证：

```yaml
app:
  ddd:
    validation:
      enabled: true
      fail-on-violation: true
      verbose-logging: true
      layers:
        anti-corruption: "..acl.."
```

## 常见违规及解决方案

### 违规1：直接在领域层使用外部模型
```java
// ❌ 错误：领域服务直接使用外部模型
@DomainService
public class OrderService {
    public void processOrder(ThirdPartyOrder order) { // 外部模型
        // ...
    }
}
```

**解决方案**：通过防腐层进行模型转换
```java
// ✅ 正确：通过防腐层隔离
@DomainService
public class OrderService {
    private final OrderGateway orderGateway; // 领域接口
    
    public void processOrder(Order order) { // 领域模型
        orderGateway.submitOrder(order);
    }
}
```

### 违规2：外部异常泄露
```java
// ❌ 错误：让外部异常泄露到领域层
public class OrderGatewayImpl implements OrderGateway {
    public void submitOrder(Order order) throws ThirdPartyApiException {
        // ...
    }
}
```

**解决方案**：在防腐层转换异常
```java
// ✅ 正确：转换异常类型
public class OrderGatewayImpl implements OrderGateway {
    public void submitOrder(Order order) {
        try {
            // 调用外部API
        } catch (ThirdPartyApiException e) {
            throw new OrderSubmissionException("订单提交失败", e);
        }
    }
}
```

## 验证命令

运行架构验证：
```bash
./gradlew test --tests "*ArchitectureValidator*"
```

或者在应用启动时自动验证（推荐用于开发环境）。

## 相关模式

- **适配器模式**：用于适配不兼容的接口
- **外观模式**：简化复杂的外部系统调用
- **策略模式**：支持多种外部系统实现
- **工厂模式**：创建特定的防腐层组件

防腐层是保持领域模型纯净性的重要手段，确保外部系统的变化不会影响到核心业务逻辑的稳定性。
