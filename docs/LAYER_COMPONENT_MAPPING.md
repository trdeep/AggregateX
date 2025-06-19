# DDD 架构分层组件映射规则

## 📋 概述

本文档明确定义了在 DDD 架构中各类组件应该放置在哪个层级，以及相应的命名约定，避免架构验证冲突。

## 🏗️ 分层架构组件映射

### 🎯 接口层 (Interfaces Layer)

**职责**: 系统与外界的协议适配和转换

| 组件类型 | 命名模式 | 示例 | 说明 |
|---------|---------|------|------|
| RPC服务接口 | `*RpcService` | `UserRpcService` | 远程过程调用接口 |
| API客户端 | `*ApiClient`, `*RemoteClient` | `PaymentApiClient` | 外部API调用客户端 |
| 消息监听器 | `*MessageListener`, `*MessageHandler` | `OrderMessageListener` | 消息队列监听器 |
| 协议集成 | `*Integration` (在 interfaces 包下) | `SmsIntegration` | 协议转换集成组件 |
| 系统通信DTO | `*IMessage`, `*IEvent`, `*ICommand`, `*IQuery` | `CreateOrderCommand` | 系统间通信数据传输对象 |

**包路径**: `..interfaces..`

**设计原则**:
- 纯协议转换，不包含业务逻辑
- 处理身份认证和权限验证
- 数据格式转换和校验

### 🛡️ 防腐层 (Anti-Corruption Layer)

**职责**: 隔离外部系统，防止外部模型污染领域模型

| 组件类型 | 命名模式 | 示例 | 说明 |
|---------|---------|------|------|
| 外部服务适配器 | `*Adapter` | `PaymentGatewayAdapter` | 外部服务适配 |
| 外部系统网关 | `*Gateway` | `PayPalGateway` | 外部系统访问网关 |
| 外部服务封装 | `*ExternalService`, `*ThirdPartyService` | `WeChatPayService` | 第三方服务封装 |
| 数据转换器 | `*Translator`, `*Converter` | `PaymentTranslator` | 外部数据转换 |
| 外观服务 | `*Facade` | `ShippingServiceFacade` | 复杂外部服务外观 |
| 外部集成 | `*Integration` (在 acl 包下) | `AlipayIntegration` | 外部系统集成组件 |
| 外部API封装 | `*ExternalApi` | `WeChatExternalApi` | 外部API封装 |
| 第三方组件 | `*ThirdParty*` | `ThirdPartyPayment` | 第三方系统组件 |

**包路径**: `..infrastructure.acl..`

**设计原则**:
- 实现领域层定义的接口
- 转换外部异常为领域异常
- 隔离外部技术细节
- 数据模型转换和验证

### 🎨 表现层 (Presentation Layer)

**职责**: 用户界面交互和数据展示

| 组件类型 | 命名模式 | 示例 | 说明 |
|---------|---------|------|------|
| Web控制器 | `*Controller` | `UserController` | MVC控制器 |
| REST控制器 | `*RestController` | `UserRestController` | RESTful API控制器 |
| 请求DTO | `*Request` | `CreateUserRequest` | HTTP请求数据传输对象 |
| 响应DTO | `*Response`, `*Result` | `UserInfoResponse` | HTTP响应数据传输对象 |
| 视图模型 | `*ViewModel`, `*VM` | `UserViewModel` | 前端视图模型 |
| 表单对象 | `*Form` | `UserRegistrationForm` | 表单数据对象 |
| 校验器 | `*Validator` | `UserInputValidator` | 输入数据校验器 |

**包路径**: `..presentation..`

### ⚙️ 应用层 (Application Layer)

**职责**: 业务流程编排和事务管理

| 组件类型 | 命名模式 | 示例 | 说明 |
|---------|---------|------|------|
| 应用服务 | `*Service`, `*ApplicationService`, `*Impl` | `UserApplicationService` | 业务用例编排 |
| 应用DTO | `*DTO`, `*Dto`, `*VO`, `*Vo` | `UserDTO` | 应用层数据传输对象 |

**包路径**: `..application..`

### 🏛️ 基础设施层 (Infrastructure Layer)

**职责**: 技术实现和持久化

| 组件类型 | 命名模式 | 示例 | 说明 |
|---------|---------|------|------|
| 仓储实现 | `*RepositoryImpl`, `*Jpa*Repository` | `UserRepositoryImpl` | 数据持久化实现 |
| 事件发布器 | `*EventPublisher` | `DomainEventPublisher` | 事件发布实现 |

**包路径**: `..infrastructure..`

## 🔍 Integration 组件的区分原则

### 接口层 Integration vs 防腐层 Integration

| 特征 | 接口层 Integration | 防腐层 Integration |
|-----|-------------------|-------------------|
| **包路径** | `..interfaces..` | `..infrastructure.acl..` |
| **主要职责** | 协议转换和适配 | 外部系统隔离和防腐 |
| **典型场景** | HTTP API接入、消息队列监听 | 第三方支付、外部ERP系统 |
| **数据处理** | 协议格式转换 | 领域模型转换和验证 |
| **依赖关系** | 调用表现层或应用层 | 实现领域层接口 |

### 示例对比

```java
// 接口层 Integration - 协议转换
@Component
public class SmsIntegration {
    // 纯协议转换，将HTTP请求转换为内部调用
    public void handleSmsRequest(HttpRequest request) {
        SmsCommand command = convertToCommand(request);
        applicationService.sendSms(command);
    }
}

// 防腐层 Integration - 外部系统防腐
@Component  
public class AlipayIntegration implements PaymentGateway {
    // 外部系统隔离，转换外部模型为领域模型
    public PaymentResult process(Payment payment) {
        AlipayRequest request = translator.toAlipayRequest(payment);
        AlipayResponse response = alipayClient.createPayment(request);
        return translator.toDomainResult(response);
    }
}
```

## ✅ 验证规则

架构验证器会检查以下规则：

1. **组件位置验证**: 确保组件在正确的层级包下
2. **命名约定验证**: 确保组件遵循命名规范  
3. **依赖关系验证**: 确保层级间依赖关系正确
4. **注解一致性验证**: 确保使用正确的Spring注解

## 🚨 常见错误

1. **Gateway放在接口层**: Gateway应该在防腐层
2. **Adapter放在接口层**: Adapter应该在防腐层  
3. **Controller放在应用层**: Controller应该在表现层
4. **Integration归属混乱**: 需要根据职责确定层级

## 📚 参考

- [防腐层规则详细文档](./ANTI_CORRUPTION_LAYER_RULES.md)
- [DDD架构原则](../README.md)
