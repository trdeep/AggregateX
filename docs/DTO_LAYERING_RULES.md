# DTO分层架构规则

## 修复说明

在DDD架构验证器中，原本存在严重的DTO命名规则冲突，导致同一个类名模式被要求同时存在于多个不同的层中。现已修复这些冲突。

## 原始问题

**冲突的规则：**
1. 应用层要求：所有 `.*DTO`、`.*Dto`、`.*VO`、`.*Vo` 都应该在应用层
2. 表现层要求：`.*DTO`、`.*VO` 都应该在表现层  
3. 防腐层要求：`.*DTO`、`.*Dto` 都应该在防腐层

这些规则会导致架构验证失败，因为同一个命名模式不能同时满足多个层级的要求。

## 修复后的分层规则

### 🎯 领域层 (Domain Layer)
**命名约定：**
- `*Value` - 值对象
- `*ValueObject` - 值对象
- `*DomainVO` - 领域值对象
- `*Event` - 领域事件（仅在领域层）
- `*DomainEvent` - 明确的领域事件

**职责：** 核心业务概念的值对象，不可变，体现业务规则；领域事件表示业务状态变化

### 📋 应用层 (Application Layer) 
**命名约定：**
- `*Command` - 命令对象
- `*Query` - 查询对象
- `*AppDTO` - 应用层数据传输对象
- `*ApplicationDTO` - 应用层数据传输对象
- `*Result` - 结果对象

**职责：** 用例编排，命令查询分离，业务流程协调

### 🎨 表现层 (Presentation Layer)
**命名约定：**
- `*Request` - HTTP请求对象
- `*Response` - HTTP响应对象
- `*ViewModel` - 视图模型
- `*Form` - 表单对象
- `*WebDTO` - Web数据传输对象
- `*PresentationDTO` - 表现层数据传输对象

**职责：** 用户界面数据交换，HTTP协议处理，输入验证

### 🔌 接口层 (Interfaces Layer)
**命名约定：**
- `*Message` - 系统间消息
- `*IntegrationEvent` - 集成事件（区分领域事件）
- `*ExternalEvent` - 外部事件
- `*RemoteCommand` - 远程命令
- `*RemoteQuery` - 远程查询
- `*IntegrationDTO` - 集成数据传输对象
- `*ExternalDTO` - 外部数据传输对象
- `*ApiRequest` - API请求对象
- `*ApiResponse` - API响应对象

**职责：** 系统间通信，消息队列处理，RPC服务接口

### 🛡️ 防腐层 (Anti-Corruption Layer)
**命名约定：**
- `*ExternalDTO` - 外部系统数据传输对象
- `*ThirdPartyDTO` - 第三方数据传输对象
- `*AdapterDTO` - 适配器数据传输对象
- `*GatewayDTO` - 网关数据传输对象
- `*ExternalData` - 外部数据对象

**职责：** 外部系统集成，数据格式转换，外部模型隔离

## 架构约束优势

### ✅ 避免冲突
- 每个层级有独特的命名前缀/后缀
- 明确的职责边界
- 清晰的依赖方向

### ✅ 提高可维护性
- 通过命名即可识别组件归属
- 便于架构治理和重构
- 减少跨层耦合

### ✅ 符合DDD原则
- 保持领域层纯净性
- 实现依赖倒置
- 支持聚合边界管理

## 使用示例

```java
// ✅ 正确示例
// 领域层
public class Money implements ValueObject { }
public class UserDomainVO { }
public class UserCreatedEvent { }       // 领域事件
public class OrderCompletedDomainEvent { } // 明确的领域事件

// 应用层  
public class CreateUserCommand { }
public class UserQuery { }
public class UserAppDTO { }

// 表现层
public class CreateUserRequest { }
public class UserResponse { }
public class UserWebDTO { }

// 接口层
public class UserMessage { }
public class OrderIntegrationEvent { }    // 集成事件
public class PaymentExternalEvent { }     // 外部事件
public class CreateUserRemoteCommand { }
public class UserRemoteQuery { }
public class UserIntegrationDTO { }
public class PaymentApiRequest { }
public class PaymentApiResponse { }

// 防腐层
public class PayPalExternalDTO { }
public class WeChatAdapterDTO { }
```

```java
// ❌ 错误示例（会产生冲突）
// 多个层使用相同的通用命名
public class UserDTO { } // 在应用层
public class UserDTO { } // 在表现层 - 冲突！
public class UserVO { }  // 在领域层
public class UserVO { }  // 在表现层 - 冲突！
```

## 验证规则实现

架构验证器现在使用特定的命名模式来避免冲突：

```java
// 应用层规则
classes().that().haveNameMatching(".*Command")
    .or().haveNameMatching(".*Query")
    .or().haveNameMatching(".*AppDTO")
    .should().resideInAPackage(getApplicationLayer())

// 表现层规则  
classes().that().haveNameMatching(".*Request")
    .or().haveNameMatching(".*Response")
    .or().haveNameMatching(".*WebDTO")
    .should().resideInAPackage(getPresentationLayer())

// 防腐层规则
classes().that().haveNameMatching(".*ExternalDTO")
    .or().haveNameMatching(".*AdapterDTO")
    .should().resideInAPackage(getAntiCorruptionLayer())
```

## 迁移指南

### 现有代码迁移
1. **识别冲突类**：找出使用通用命名的DTO类
2. **确定层级归属**：根据实际职责确定正确的层级
3. **重命名类**：使用新的命名约定
4. **更新引用**：修改所有引用这些类的代码

### 渐进式迁移
- 新代码立即采用新命名约定
- 现有代码可以逐步重构
- 使用IDE重构工具批量处理

这个修复确保了DDD架构验证器能够正确执行，避免了命名冲突，提高了代码架构的清晰性和可维护性。
