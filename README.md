# AggregateX - DDD框架

AggregateX 是一个基于领域驱动设计（DDD）原则构建的 Java 框架，为开发复杂业务系统提供完整的架构支持。本框架整合了 CQRS 模式、事件溯源、事件总线等 DDD 核心概念，帮助开发团队更好地实现领域驱动设计。

> 很粗糙的开始，持续完善中...
> 个人维护，有缘人可以借鉴参考学习，互相学习 🤝

[![Latest Release](https://img.shields.io/github/v/release/trdeep/AggregateX)](https://github.com/trdeep/AggregateX/releases)
[![Status](https://img.shields.io/badge/status-开发中-orange.svg)](https://github.com/trdeep/AggregateX)
[![JDK Version](https://img.shields.io/badge/JDK-21+-blue.svg)](https://openjdk.java.net/projects/jdk/21/)
[![License](https://img.shields.io/badge/license-MPL--2.0-green.svg)](https://www.mozilla.org/en-US/MPL/2.0/)

## 🚀 核心特性

### 1. 完整的DDD分层架构

#### 领域层 (Domain Layer)
- **聚合根（Aggregate Root）**: 确保业务不变性，管理领域对象生命周期
- **实体（Entity）**: 具有唯一标识的领域对象
- **值对象（Value Object）**: 描述领域特征的不可变对象
- **领域事件（Domain Event）**: 捕获领域中的重要变更

#### 应用层 (Application Layer)
- **命令处理**: 处理系统状态变更的请求
- **查询处理**: 处理数据查询的请求
- **应用服务**: 协调领域对象和基础设施

#### 基础设施层 (Infrastructure Layer)
- **持久化实现**: 提供数据存储能力
- **消息实现**: 提供事件发布和订阅
- **外部服务集成**: 集成第三方服务和系统

### 2. CQRS实现

- **命令（Command）**: 处理系统状态变更
  - 严格的数据验证
  - 事务管理
  - 并发控制

- **查询（Query）**: 获取系统状态
  - 优化的数据读取
  - 灵活的查询条件
  - 支持复杂数据组装

### 3. 智能代码生成器

内置强大的DDD模块代码生成器，提供：

- **一键生成完整模块**: 根据模块名自动生成DDD分层架构代码
- **标准化模板**: 基于FreeMarker的可定制模板引擎
- **交互式界面**: 友好的命令行交互体验
- **智能命名**: 自动处理各种命名格式转换

#### 快速生成模块

```bash
# 运行代码生成器
java -cp aggregatex.jar cn.treedeep.king.tools.DDDModuleGenerator

# 输入模块信息
📦 请输入模块名称: user 用户管理
```

生成完整的DDD模块结构，包括聚合根、命令处理器、查询处理器、REST控制器等。

### 4. 事件溯源与智能缓存

#### 存储实现
- **多级存储**: 内存存储（开发测试）、JPA存储（生产环境）
- **事件压缩**: 智能事件流压缩，优化存储空间
- **快照机制**: 定期创建聚合状态快照，提升重建性能
- **事件归档**: 自动归档历史事件，保持主表性能

#### 智能缓存系统
基于Caffeine的多级缓存策略：
- **事件流缓存**: 10,000条/1小时
- **快照缓存**: 5,000条/2小时  
- **归档缓存**: 20,000条/24小时
- **聚合缓存**: 20,000条/24小时

### 4. 架构验证系统

基于ArchUnit的DDD架构约束自动检查：

- **分层依赖**: 检查层间依赖关系
- **聚合根规则**: 验证聚合根设计原则
- **仓储模式**: 检查仓储接口和实现
- **命名约定**: 验证类和包命名规范
- **包结构**: 检查DDD包结构约定

### 5. 智能代码生成器

内置强大的DDD模块代码生成器，极大提升开发效率：

#### 核心特性

- **完整的DDD分层结构**: 自动生成领域层、应用层、基础设施层、表现层的标准代码
- **基于FreeMarker模板**: 灵活可定制的代码模板系统
- **交互式命令行**: 用户友好的交互式生成界面
- **智能命名转换**: 自动处理PascalCase、camelCase等命名转换
- **包结构规范**: 严格遵循DDD分层架构和包命名约定

#### 生成的文件类型

- **领域层**: 聚合根、实体ID、仓储接口、领域事件、领域服务
- **应用层**: 命令/查询对象、处理器、DTO、转换器
- **基础设施层**: JPA仓储、仓储实现、模块配置
- **表现层**: REST控制器、请求/响应DTO

#### 使用方法

**交互式模式**（推荐）:

```bash
# 直接运行生成器主类
java -cp build/libs/AggregateX-1.0.0.jar cn.treedeep.king.tools.DDDModuleGenerator

# 或通过Gradle运行
./gradlew -PmainClass=cn.treedeep.king.tools.DDDModuleGenerator run
```

**编程式调用**:

```java
DDDModuleGenerator generator = new DDDModuleGenerator();
generator.generateModule("/path/to/project", "user 用户管理");
```

#### 生成示例

执行生成器后，会创建完整的模块结构：

```text
src/main/java/cn/treedeep/king/user/
├── domain/
│   ├── User.java                    # 聚合根
│   ├── UserId.java                  # 实体ID
│   ├── UserRepository.java         # 仓储接口
│   ├── event/
│   │   └── UserCreatedEvent.java   # 领域事件
│   └── service/
│       └── UserDomainService.java  # 领域服务
├── application/
│   ├── command/
│   │   ├── CreateUserCommand.java        # 创建命令
│   │   └── CreateUserCommandHandler.java # 命令处理器
│   ├── query/
│   │   ├── UserListQuery.java            # 查询对象
│   │   ├── UserListQueryResult.java      # 查询结果
│   │   └── UserListQueryHandler.java     # 查询处理器
│   └── dto/
│       ├── UserDto.java                  # 数据传输对象
│       └── UserDtoConverter.java         # DTO转换器
├── infrastructure/
│   ├── repository/
│   │   ├── UserJpaRepository.java        # JPA仓储
│   │   └── UserRepositoryImpl.java       # 仓储实现
│   └── ModuleConfig.java                 # 模块配置
└── presentation/
    ├── UserController.java               # REST控制器
    └── dto/
        ├── CreateUserRequest.java        # 创建请求DTO
        └── UserListResponse.java         # 列表响应DTO
```

### 6. 全面监控体系

提供生产级的监控和性能指标：

```java
// 事件存储监控
eventstore.events.saved          // 已保存的事件总数
eventstore.events.read           // 已读取的事件总数
eventstore.snapshots.created     // 已创建的快照总数

// 命令执行监控
commands.executed.total          // 命令执行总数
commands.executed.success        // 成功执行数量
commands.execution.time          // 命令执行耗时

// 缓存性能监控
cache.requests.total             // 缓存请求总数
cache.hits.total                 // 缓存命中总数
cache.misses.total              // 缓存未命中总数
```

## 📦 安装
> 未正式发布，请自行打包本地安装。

### Gradle

```gradle
dependencies {
    implementation 'cn.treedeep:aggregatex-ddd-framework:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>cn.treedeep</groupId>
    <artifactId>aggregatex-ddd-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚨 重要要求

**使用此框架的子项目必须使用 `cn.treedeep.king` 作为基础包名**

这是框架自动配置和组件扫描的必要条件。项目结构应该类似：

```
src/main/java/
└── cn/
    └── treedeep/
        └── king/
            └── your-project/
                ├── application/     # 应用层
                ├── domain/          # 领域层
                ├── infrastructure/  # 基础设施层
                └── interfaces/      # 接口层
                └── presentation/    # 表现层
```

## 🏗️ 快速开始

### 1. 启用框架

在主应用类上添加 `@EnableAggregateX` 注解：

```java
@SpringBootApplication
@EnableAggregateX
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 2. 使用代码生成器

快速生成标准的DDD模块结构：

```bash
# 运行代码生成器
java -cp build/libs/AggregateX-1.0.0.jar cn.treedeep.king.tools.DDDModuleGenerator

# 交互式输入
📁 请输入项目路径 (默认为当前路径 '.'): 
📦 请输入模块名称，可空格带注释 (如: user 用户, order 订单): user 用户管理
```

生成器将自动创建完整的模块结构，包括：

- 聚合根和实体ID
- 命令/查询处理器  
- REST控制器
- JPA仓储实现
- 标准DTO和转换器

### 3. 核心配置

```yaml
# application.yml
spring:
  cache:
    type: caffeine
    cache-names: [events, snapshots, archives, aggregates]
    caffeine:
      spec: maximumSize=10000,expireAfterWrite=3600s

app:
  event-store:
    type: jpa             # 事件存储类型：memory/jpa
    table-name: events    # 事件表名称
    batch-size: 1000      # 批量操作大小
    snapshot:
      enabled: true       # 是否启用快照
      frequency: 100      # 快照频率（事件数）
  
  cqrs:
    async:
      enabled: true
      pool-size: 10
    retry:
      max-attempts: 3
      delay: 1000
    validation:
      enabled: true
  
  event-bus:
    type: simple
    retry:
      max-attempts: 3
      delay: 500

  architecture:
    validation:
      enabled: true                # 是否启用架构验证
      fail-on-violation: true      # 发现违规时是否停止启动
```

### 4. 完整示例：用户管理模块

#### 聚合根

```java
@Entity
@Table(name = "users")
public class User extends AggregateRoot<UserId> {
    @EmbeddedId
    private UserId id;
    
    @Column(unique = true)
    private String username;
    
    @Column(unique = true) 
    private String email;
    
    private String hashedPassword;
    private UserStatus status;
    private OffsetDateTime createdAt;

    public static User register(String username, String email, String password) {
        User user = new User();
        user.id = UserId.newId();
        user.username = username;
        user.email = email;
        user.hashedPassword = hashPassword(password);
        user.status = UserStatus.ACTIVE;
        user.createdAt = OffsetDateTime.now();
        
        // 发布领域事件
        user.registerEvent(new UserRegisteredEvent(user.id, username, email));
        return user;
    }
    
    public void login() {
        if (status != UserStatus.ACTIVE) {
            throw new ValidationException("用户状态异常，无法登录");
        }
        this.lastLoginAt = OffsetDateTime.now();
        registerEvent(new UserLoggedInEvent(this.id, username));
    }
}
```

#### 命令处理器

```java
@Component
@Transactional
public class RegisterUserCommandHandler extends AbstractCommandHandler<RegisterUserCommand> {
    private final UserRepository userRepository;

    @Override
    protected Object doHandle(RegisterUserCommand command) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(command.getUsername()).isPresent()) {
            throw new BusinessRuleViolationException("用户名已存在");
        }
        
        User user = User.register(
            command.getUsername(), 
            command.getEmail(), 
            command.getPassword()
        );
        
        userRepository.save(user);
        return user.getId();
    }
}
```

#### 事件处理器

```java
@Component
public class UserEventHandler extends AbstractEventHandler<UserRegisteredEvent> {
    private final EmailService emailService;
    
    @Override
    protected void doHandle(UserRegisteredEvent event) {
        // 发送欢迎邮件
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
        
        log.info("用户注册事件已处理: {}", event.getAggregateId());
    }
}
```

#### REST API

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户注册、登录和查询接口")
public class UserController {
    private final CommandBus commandBus;
    private final UserQueryService userQueryService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterUserRequest request) {
        
        RegisterUserCommand command = new RegisterUserCommand(
            request.getUsername(),
            request.getEmail(), 
            request.getPassword()
        );
        
        commandBus.send(command);
        return ResponseEntity.ok(ApiResponse.success("用户注册成功"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable UserId userId) {
        UserDTO user = userQueryService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
```
### 代码生成器最佳实践

#### 1. 模块命名规范

- **使用有意义的业务名称**: `user`、`order`、`product` 而不是 `module1`、`test`
- **支持中文注释**: `user 用户管理`、`order 订单处理` 提供更好的代码注释
- **遵循单数形式**: 使用 `product` 而不是 `products`

#### 2. 生成后的调整建议

- **完善聚合根业务逻辑**: 根据具体业务需求添加领域方法
- **调整实体属性**: 修改生成的基础属性以符合业务模型
- **实现领域事件处理**: 添加具体的事件处理逻辑
- **编写单元测试**: 为生成的代码添加完整的测试覆盖

#### 3. 模板定制

如需定制生成的代码风格，可以修改模板文件：

```bash
# 模板文件位置
src/main/resources/template/
├── domain/Main.java.ftl           # 聚合根模板
├── application/command/CreateCommand.java.ftl  # 命令模板
└── presentation/Controller.java.ftl            # 控制器模板
```

修改模板后重新运行生成器即可应用新的代码风格。

## 📚 最佳实践

### 聚合根设计原则

- **保持聚合边界清晰**: 一个聚合根管理一个业务概念
- **使用强类型标识符**: 如UserId而不是原始的String类型
- **通过领域方法封装业务逻辑**: 避免直接操作属性
- **合理使用版本控制**: 防止并发修改冲突

```java
// 好的做法
public class User extends AggregateRoot<UserId> {
    public void changeEmail(String newEmail) {
        validateEmailFormat(newEmail);
        checkNotDeleted();
        
        String oldEmail = this.email;
        this.email = newEmail;
        
        registerEvent(new UserEmailChangedEvent(getId(), oldEmail, newEmail));
    }
}

// 避免的做法 - 缺少验证和事件
public void setEmail(String email) {
    this.email = email;
}
```

### 事件设计原则

- **事件名称使用过去时**: 描述已发生的事实
- **事件数据包含足够信息**: 但避免冗余
- **事件应该是不可变的**: 一旦创建不应修改

```java
// 好的事件设计
public class UserEmailChangedEvent extends DomainEvent {
    private final String oldEmail;
    private final String newEmail;
    private final Instant changedAt;
    
    // 构造函数和getter...
}
```

## 🔧 代码生成器详细指南

### 交互式使用

代码生成器提供友好的交互式界面：

```text
    ___                                  __      _  __
   /   |  ____ _____ ________  ____ _____ _/ /____ | |/ /
  / /| | / __ `/ __ `/ ___/ _ \/ __ `/ __ `/ __/ _ \|   / 
 / ___ |/ /_/ / /_/ / /  /  __/ /_/ / /_/ / /_/  __/   |  
/_/  |_|\__, /\__, /_/   \___/\__, /\__,_/\__/\___/_/|_|  
       /____//____/         /____/                      

DDD Module Generator v1.0.0

🎯 AggregateX DDD模块生成器
═══════════════════════════

📁 请输入项目路径 (默认为当前路径 '.'): 
📦 请输入模块名称，可空格带注释 (如: user 用户, order 订单): product 商品管理

🚀 开始生成模块...
📁 创建目录结构...
📝 生成模板文件...
✅ 模块 'product' 生成完成
📍 模块位置: /path/to/project/src/main/java/cn/treedeep/king/product

💡 下一步操作建议:
   1. 根据具体业务需求调整聚合根和值对象
   2. 完善领域事件和业务规则
   3. 添加单元测试和集成测试
   4. 配置数据库迁移脚本
   5. 更新API文档

🔧 相关命令:
   • 编译项目: ./gradlew build
   • 运行测试: ./gradlew test
   • 生成文档: ./gradlew javadoc
```

### 生成的代码结构

以生成`product`模块为例，生成器会创建以下标准DDD结构：

#### 领域层 (Domain Layer)

```java
// ProductId.java - 强类型标识符
@Embeddable
public class ProductId {
    private String value;
    
    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString());
    }
}

// Product.java - 聚合根
@Entity
@Table(name = "products")
public class Product extends AggregateRoot<ProductId> {
    @EmbeddedId
    private ProductId id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    public static Product create(String name, String description) {
        Product product = new Product();
        product.id = ProductId.generate();
        product.name = name;
        product.description = description;
        
        product.registerEvent(new ProductCreatedEvent(product.id, name));
        return product;
    }
}
```

#### 应用层 (Application Layer)

```java
// CreateProductCommand.java - 命令对象
public class CreateProductCommand {
    private final String name;
    private final String description;
    // 构造函数和getter...
}

// CreateProductCommandHandler.java - 命令处理器
@Component
@Transactional
public class CreateProductCommandHandler extends AbstractCommandHandler<CreateProductCommand> {
    private final ProductRepository productRepository;
    
    @Override
    protected Object doHandle(CreateProductCommand command) {
        Product product = Product.create(command.getName(), command.getDescription());
        productRepository.save(product);
        return product.getId();
    }
}
```

#### 基础设施层 (Infrastructure Layer)

```java
// ProductJpaRepository.java - JPA仓储接口
@Repository
public interface ProductJpaRepository extends JpaRepository<Product, ProductId> {
    Optional<Product> findByName(String name);
}

// ProductRepositoryImpl.java - 仓储实现
@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;
    
    @Override
    public void save(Product product) {
        jpaRepository.save(product);
    }
}
```

#### 表现层 (Presentation Layer)

```java
// ProductController.java - REST控制器
@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理", description = "商品相关接口")
public class ProductController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    
    @PostMapping
    @Operation(summary = "创建商品")
    public ResponseEntity<Result<Void>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(request.getName(), request.getDescription());
        commandBus.send(command);
        return ResponseEntity.ok(Result.success());
    }
}
```

### 自定义模板

生成器基于FreeMarker模板引擎，支持自定义模板：

```text
src/main/resources/template/
├── domain/
│   ├── Main.java.ftl              # 聚合根模板
│   ├── Id.java.ftl                # 实体ID模板
│   └── Repository.java.ftl        # 仓储接口模板
├── application/
│   ├── command/
│   │   ├── CreateCommand.java.ftl      # 命令模板
│   │   └── CreateCommandHandler.java.ftl # 命令处理器模板
│   └── dto/
│       └── Dto.java.ftl               # DTO模板
└── presentation/
    └── Controller.java.ftl            # 控制器模板
```

可以根据项目需求修改这些模板文件来定制生成的代码风格。

## 🔧 故障排除

### 常见问题

#### Q: 聚合未正确持久化

检查以下项目：
- 实体类是否标注了正确的JPA注解
- 仓储是否实现了`doSave`方法
- 是否在事务边界内调用save方法

#### Q: 事件未被处理

检查以下项目：
- 事件处理器是否被Spring容器管理（添加`@Component`注解）
- 事件处理器是否继承了`AbstractEventHandler`
- 是否启用了事件总线配置

#### Q: 架构验证失败

检查包结构是否符合DDD分层架构要求：
- domain包：聚合根、值对象、领域服务
- application包：应用服务、命令处理器
- infrastructure包：仓储实现、外部服务集成

### 调试技巧

启用详细日志：

```yaml
logging:
  level:
    cn.treedeep.king: DEBUG
    org.springframework.cache: DEBUG
    org.springframework.transaction: DEBUG
```

查看监控指标：

```bash
# 访问监控端点
curl http://localhost:8080/actuator/metrics/aggregate.operation.duration
curl http://localhost:8080/actuator/prometheus
```
- 是否在事务边界内调用save方法

## 📖 示例项目

完整的使用示例请参考：

- `AggregateX/example` 模块
- `src/test` 目录下的测试用例
- **代码生成器**: 使用 `DDDModuleGenerator` 快速生成标准DDD模块

### 代码生成器示例

```bash
# 1. 运行生成器
java -cp build/libs/AggregateX-1.0.0.jar cn.treedeep.king.tools.DDDModuleGenerator

# 2. 输入模块信息
📦 请输入模块名称: product 商品管理

# 3. 自动生成完整的DDD结构
✅ 模块 'product' 生成完成
```

生成的模块包含完整的CQRS实现、REST API、JPA仓储等标准组件。

## 📚 相关文档

- [维基百科 - 领域驱动设计](https://zh.wikipedia.org/wiki/%E9%A0%98%E5%9F%9F%E9%A9%85%E5%8B%95%E8%A8%AD%E8%A8%88)
- [DDD 概念参考](https://domain-driven-design.org/zh/ddd-concept-reference.html)
- [领域驱动设计DDD在B端营销系统的实践](https://tech.meituan.com/2024/05/27/ddd-in-business.html)
- [DDD领域驱动设计基本理论知识总结](https://www.cnblogs.com/netfocus/archive/2011/10/10/2204949.html)
- [DDD从理论到实践](https://blog.csdn.net/qq_41889508/article/details/124907312)
- [领域驱动设计](https://tonydeng.github.io/2022/11/06/domain-driven-design/)
- [DDD（Domain-Driven Design，领域驱动设计）](https://www.cnblogs.com/sTruth/p/17760144.html)

## 🚀 监控与运维

框架内置了完整的监控指标，支持：

- 聚合操作性能监控
- 事件存储性能统计
- 命令处理延迟追踪
- 缓存命中率统计

访问监控端点：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 性能指标
curl http://localhost:8080/actuator/metrics

# Prometheus格式指标
curl http://localhost:8080/actuator/prometheus
```

## 🆕 版本历史

### v1.3.0 (2025-06) - 当前版本

- **智能代码生成器**: 内置DDD模块代码生成工具，支持交互式和编程式调用
- **模板引擎集成**: 基于FreeMarker的灵活模板系统
- **完整文件生成**: 自动生成领域层、应用层、基础设施层、表现层的标准代码
- **智能命名处理**: 自动处理各种命名格式转换和包结构规范

### v1.2.0 (2025-06)

- **架构验证系统**: 内置15+项DDD架构验证规则
- **智能缓存系统**: 基于Caffeine的多级缓存策略
- **全面监控体系**: 事件存储、命令执行、缓存性能分析
- **用户管理模块**: 完整的CQRS实现示例

### v1.1.0 (2025-05)

- 添加多级缓存支持
- 增强监控能力
- 新增缓存相关指标

### v1.0.0 (2025-04)

- 实现核心DDD组件
- 提供基础设施支持

## 🤝 贡献

欢迎提交Issue和Pull Request来帮助改进这个框架。

## 📄 许可证

本项目采用 [MPL 2.0 License](https://www.mozilla.org/en-US/MPL/2.0/) 许可证。

## 🏢 关于我们

TreeDeep Team - 专注于企业级Java基础架构

- 网站: <https://treedeep.cn>
- 邮箱: <shushen@treedeep.cn>
