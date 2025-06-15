# AggregateX 框架代码模板与功能参考

## 框架概述

AggregateX 是一个基于 DDD（领域驱动设计）的 Java 代码生成框架，提供完整的 DDD 项目结构生成能力。框架采用模板引擎技术，可以根据 JSON 配置或 Java DSL 快速生成标准化的 DDD 代码结构。

## 核心功能特性

### 1. 代码生成能力
- **聚合根 (AggregateRoot)**: 完整的聚合根类，包含乐观锁、软删除、事件支持
- **实体 (Entity)**: 领域实体类，支持值对象嵌套
- **值对象 (ValueObject)**: 不可变值对象，支持 JSON 转换
- **仓储 (Repository)**: 三层仓储架构（接口、实现、JPA）
- **领域服务 (DomainService)**: 领域服务接口和实现
- **应用服务 (ApplicationService)**: 应用层服务
- **领域事件 (DomainEvent)**: 事件驱动架构支持

### 2. DDD 最佳实践
- **聚合边界管理**: 自动生成聚合根标识符和边界控制
- **事件驱动架构**: 内置领域事件注册和发布机制
- **值对象嵌套**: 支持 `@Embedded` 注解的值对象嵌入
- **乐观锁控制**: 自动版本号管理和并发控制
- **审计字段**: 自动创建时间和修改时间管理

### 3. 持久化支持
- **JPA 集成**: 完整的 JPA 注解和配置
- **数据库映射**: 自动表名、字段名映射
- **软删除**: 标记删除而非物理删除
- **事件存储**: 支持事件溯源模式

## 生成的代码结构

```
项目根目录/
├── src/main/java/包名/模块名/
│   ├── domain/                          # 领域层
│   │   ├── AggregateRoot.java          # 聚合根
│   │   ├── AggregateRootId.java        # 聚合根标识符
│   │   ├── Entity.java                 # 实体
│   │   ├── ValueObject.java            # 值对象
│   │   ├── service/                    # 领域服务
│   │   │   ├── DomainService.java      # 领域服务接口
│   │   │   └── DomainServiceImpl.java  # 领域服务实现
│   │   └── event/                      # 领域事件
│   │       └── DomainEvent.java        # 具体事件
│   ├── application/                     # 应用层
│   │   ├── ApplicationService.java     # 应用服务接口
│   │   └── ApplicationServiceImpl.java # 应用服务实现
│   └── infrastructure/                  # 基础设施层
│       ├── Repository.java             # 仓储接口
│       ├── RepositoryImpl.java         # 仓储实现
│       └── JpaRepository.java          # JPA 仓储
```

## 代码模板详解

### 1. 聚合根模板 (AggregateRoot.java.ftl)

```java
// 生成的聚合根类特征
@Entity
@Table(name = "${tableName}")
@Comment("${comment}表")
@NoArgsConstructor
@Getter
@Setter
public class ${className} extends AggregateRoot<${className}Id> {
    
    @EmbeddedId
    private ${className}Id id;
    
    // 普通属性 - REGULAR 类型
    @Comment("${comment}")
    private String ${propertyName};
    
    // 值对象属性 - 使用 @Embedded 注解
    @Embedded
    @Comment("${comment}")
    private ${ValueObjectType} ${propertyName};
    
    // 领域方法
    public ${returnType} ${methodName}(${parameters}) {
        // 业务逻辑实现
        this.registerEvent(new ${EventName}(this.id, ${params}));
    }
}
```

**关键特性:**
- 继承框架基础聚合根类
- 自动乐观锁版本控制
- 内置领域事件注册机制
- 支持软删除功能
- 审计字段自动管理

### 2. 值对象模板 (ValueObject.java.ftl)

```java
// 生成的值对象类特征
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ${className} extends ValueObjectBase {
    
    /**
     * ${comment}
     */
    private String ${propertyName};
    
    // 多属性值对象支持 JSON 转换器
    public static class ${className}JsonConverter implements AttributeConverter<${className}, String> {
        @Override
        public String convertToDatabaseColumn(${className} attribute) {
            return JsonUtil.toJson(attribute);
        }
        
        @Override
        public ${className} convertToEntityAttribute(String dbData) {
            return JsonUtil.fromJson(dbData, ${className}.class);
        }
    }
}
```

**关键特性:**
- 不可变设计原则
- 自动 JSON 序列化支持
- JPA @Embeddable 注解
- 值语义 equals/hashCode

### 3. 实体模板 (Entity.java.ftl)

```java
// 生成的实体类特征
@Entity
@Table(name = "${tableName}s")
@Comment("${comment}表")
@Getter
@NoArgsConstructor
public class ${className} extends EntityBase<UIdentifier> {
    
    @EmbeddedId
    @Comment("ID")
    private UIdentifier id;
    
    // 普通属性
    @Comment("${comment}")
    private String ${propertyName};
    
    // 值对象属性嵌套
    @Embedded
    @Comment("${comment}")
    private ${ValueObjectType} ${propertyName};
}
```

### 4. 仓储模板

#### 仓储接口 (Repository.java.ftl)
```java
public interface ${className}Repository extends AggregateRepository<${className}, ${className}Id> {
    
    // 标准 CRUD 方法已在父接口中定义
    
    // 业务查询方法
    Optional<${className}> findByUsername(String username);
    List<${className}> findByStatus(String status);
}
```

#### 仓储实现 (RepositoryImpl.java.ftl)
```java
@Repository
@RequiredArgsConstructor
public class ${className}RepositoryImpl extends AbstractRepository<${className}, ${className}Id> 
    implements ${className}Repository {
    
    private final ${className}JpaRepository jpaRepository;
    
    @Override
    protected ${className} doLoad(${className}Id id) {
        return jpaRepository.findById(id.getValue()).orElse(null);
    }
    
    @Override
    protected void doSave(${className} aggregate) {
        jpaRepository.save(aggregate);
    }
    
    @Override
    public Optional<${className}> findByUsername(String username) {
        return jpaRepository.findByUsername(username);
    }
}
```

### 5. 应用服务模板

```java
@Service
@RequiredArgsConstructor
@Transactional
public class ${className}ApplicationServiceImpl implements ${className}ApplicationService {
    
    private final ${className}Repository repository;
    private final DomainEventPublisher eventPublisher;
    
    @Override
    public void ${methodName}(${CommandType} command) {
        // 1. 参数验证
        // 2. 加载聚合根
        // 3. 执行业务操作
        // 4. 保存聚合根
        // 5. 发布领域事件
        
        ${className} aggregate = repository.findById(command.getId())
            .orElseThrow(() -> new ${className}NotFoundException(command.getId()));
            
        aggregate.${businessMethod}(command.getParams());
        
        repository.save(aggregate);
        eventPublisher.publishEvents(aggregate.getDomainEvents());
    }
}
```

## 属性类型处理机制

### REGULAR 属性
```java
// JSON 配置
{
    "name": "username",
    "comment": "用户名",
    "type": "REGULAR"
}

// 生成代码
@Comment("用户名")
private String username;
```

### VALUE_OBJECT_PROPERTY 属性
```java
// JSON 配置
{
    "name": "deviceFingerprint",
    "comment": "设备指纹",
    "type": "VALUE_OBJECT_PROPERTY"
}

// 生成代码（在实体中）
@Embedded
@Comment("设备指纹")
private DeviceFingerprint deviceFingerprint;
```

### AGGREGATE_ROOT_PROPERTY 属性
```java
// JSON 配置
{
    "name": "phone",
    "comment": "手机号",
    "type": "AGGREGATE_ROOT_PROPERTY"
}

// 生成代码（在聚合根中）
@Embedded
@Comment("手机号")
private Phone phone;
```

## 框架核心类库

### 1. 基础领域类

```java
// AggregateRoot 基类
public abstract class AggregateRoot<ID extends Identifier> {
    @Version
    private Long version;                    // 乐观锁版本号
    
    private boolean deleted;                 // 软删除标记
    private OffsetDateTime lastModifiedAt;   // 最后修改时间
    private final OffsetDateTime createdAt;  // 创建时间
    
    private final List<DomainEvent> domainEvents;     // 领域事件
    private final EntityChangeTracker changeTracker;  // 变更跟踪
    
    // 事件注册
    public void registerEvent(DomainEvent event);
    
    // 软删除
    public void markAsDeleted();
    
    // 版本控制
    public void updateVersion();
}
```

### 2. 值对象基类

```java
// ValueObjectBase 基类
public abstract class ValueObjectBase {
    // 值对象的通用行为
    // 不可变性保证
    // 值语义比较
}
```

### 3. 仓储基类

```java
// AbstractRepository 基类
public abstract class AbstractRepository<T extends AggregateRoot<ID>, ID extends Identifier> 
    implements AggregateRepository<T, ID> {
    
    // 模板方法模式
    protected abstract T doLoad(ID id);
    protected abstract void doSave(T aggregate);
    
    // 事件发布集成
    private final DomainEventPublisher eventPublisher;
    
    @Transactional
    public T save(T aggregate) {
        doSave(aggregate);
        eventPublisher.publishEvents(aggregate.getDomainEvents());
        aggregate.clearDomainEvents();
        return aggregate;
    }
}
```

## 自动识别规则

### 1. 时间字段识别
包含以下关键词的字段自动生成为 `OffsetDateTime` 类型：
- `Time`, `Date`, `At` (如: `createTime`, `birthDate`, `updatedAt`)

### 2. ID 字段识别
包含 `Id` 或 `ID` 的字段自动生成为 `UIdentifier` 类型：
- `userId`, `orderId`, `ID` 等

### 3. 命名转换规则
- **表名**: 类名转下划线 + 复数形式 (`User` → `users`)
- **字段名**: 驼峰命名保持不变
- **类名**: 帕斯卡命名 (首字母大写)

## 最佳实践模式

### 1. 聚合设计模式
```java
// 聚合根应该：
- 包含业务不变量
- 控制内部实体生命周期
- 注册领域事件
- 提供领域方法

// 避免：
- 聚合过大（超过 7±2 个实体）
- 跨聚合事务
- 暴露内部实体
```

### 2. 值对象设计模式
```java
// 值对象应该：
- 不可变设计
- 值语义比较
- 自包含验证
- 组合相关属性

// 避免：
- 包含 ID 字段
- 可变状态
- 复杂业务逻辑
```

### 3. 事件设计模式
```java
// 领域事件应该：
- 使用过去时命名
- 包含时间戳
- 携带必要业务数据
- 保持不可变

// 示例：
public class UserRegisteredEvent extends DomainEvent {
    private final UserId userId;
    private final String username;
    private final String email;
    private final OffsetDateTime registeredAt;
}
```

## 使用示例

### 1. 基于 Java DSL
```java
// 定义聚合根
var userAggregate = AggregateRoot.create("User", "用户聚合根",
    // 普通属性
    Property.create("username", "用户名"),
    Property.create("email", "邮箱"),
    
    // 聚合根级别的值对象嵌入
    Property.AggregateRootProperty.create("phone", "手机号"),
    
    // 实体定义
    Entity.create("LoginRecord", "登录记录",
        Entity.property("loginTime", "登录时间"),
        Entity.property("ipAddress", "IP地址"),
        // 实体级别的值对象嵌入
        Property.ValueObjectProperty.create("deviceFingerprint", "设备指纹")
    ),
    
    // 值对象定义
    ValueObject.create("Phone", "手机号",
        Entity.property("phoneNumber", "手机号码"),
        Entity.property("countryCode", "国家代码")
    ),
    
    // 领域方法
    AggregateRoot.method("login", "用户登录", "boolean",
        Method.parameter("password", "String", "密码")
    )
);

// 生成代码
DDDModuleGenerator generator = new DDDModuleGenerator();
generator.generateModule(
    "/path/to/project",
    "com.example",
    Module.create("user", "用户模块", userAggregate),
    "作者姓名",
    "版权信息"
);
```

### 2. 基于 JSON 配置
```java
// 加载 JSON 配置
ModuleConfigConverter converter = new ModuleConfigConverter();
List<Module> modules = converter.loadModulesFromJsonFile("config.json");

// 生成代码
DDDModuleGenerator generator = new DDDModuleGenerator();
modules.forEach(module -> {
    generator.generateModule("/path/to/project", "com.example", module, "作者", "版权");
});
```

## 扩展点

### 1. 自定义模板
- 模板位置: `src/main/resources/template/`
- 使用 FreeMarker 模板引擎
- 支持自定义模板变量

### 2. 自定义转换器
- 实现 `ModuleConfigConverter` 接口
- 支持不同配置格式 (YAML, XML 等)

### 3. 自定义生成器
- 继承 `DDDTemplateGenerator`
- 添加自定义生成逻辑

## 注意事项

1. **命名规范**: 严格遵循 Java 命名规范
2. **包结构**: 遵循标准的 DDD 分层架构
3. **依赖管理**: 确保项目包含必要的依赖 (Spring Boot, JPA, Lombok 等)
4. **数据库**: 支持所有 JPA 兼容的数据库
5. **并发控制**: 使用乐观锁避免并发冲突

## 技术栈

- **Java 17+**: 现代 Java 特性支持
- **Spring Boot 3.x**: 依赖注入和自动配置
- **Spring Data JPA**: 数据持久化
- **Hibernate**: ORM 实现
- **Lombok**: 减少样板代码
- **FreeMarker**: 模板引擎
- **Jackson**: JSON 处理
- **JUnit 5**: 单元测试

通过 AggregateX 框架，可以快速构建符合 DDD 设计原则的企业级 Java 应用，大幅提升开发效率和代码质量。
