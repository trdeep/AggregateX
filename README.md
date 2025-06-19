# AggregateX - DDD框架

AggregateX 是一个基于领域驱动设计（DDD）原则构建的 Java 框架，为开发复杂业务系统提供完整的架构支持。

> 个人维护项目，持续完善中...

[![Latest Release](https://img.shields.io/github/v/release/trdeep/AggregateX)](https://github.com/trdeep/AggregateX/releases)
[![Status](https://img.shields.io/badge/status-开发中-orange.svg)](https://github.com/trdeep/AggregateX)
[![JDK Version](https://img.shields.io/badge/JDK-21+-blue.svg)](https://openjdk.java.net/projects/jdk/21/)
[![License](https://img.shields.io/badge/license-MPL--2.0-green.svg)](https://www.mozilla.org/en-US/MPL/2.0/)

## 🚀 核心特性

### DDD分层架构

- **领域层**: 聚合根、实体、值对象、领域事件
- **应用层**: 命令/查询处理、应用服务
- **基础设施层**: 仓储实现、外部服务集成
- **表现层**: REST API控制器
- **防腐层**: 外部系统隔离、模型转换

### CQRS & 事件溯源

- 命令查询职责分离（CQRS）
- 事件驱动架构
- 智能缓存系统
- 事件存储与快照

### 智能代码生成器

内置DDD模块代码生成器，支持：

- **编程式生成**: 通过代码配置生成完整模块
- **JSON5配置**: 支持注释的配置文件
- **完整模块结构**: 自动生成DDD分层代码
- **模块文档**: 自动生成README.md

#### 使用示例

**编程式调用**:

```java
// 创建模块配置
var userModule = ModuleInfo.create("user", "用户模块")
    .addAggregateRoot(
        AggregateRoot.create("User", "用户聚合根")
            .addProperty(Property.create("username", "用户名"))
            .addProperty(Property.create("email", "邮箱"))
    );

// 生成模块
DDDModuleGenerator generator = new DDDModuleGenerator();
generator.generateModules("/path/to/project", "com.example", 
    List.of(userModule), false);
```

**JSON5配置文件**:

```json5
// user-module.json5
[
  {
    "name": "user",
    "comment": "用户模块",
    "remarks": "用户管理模块，负责用户注册、登录等功能",
    "aggregateRoots": [
      {
        "name": "User",
        "comment": "用户聚合根",
        "properties": [
          {
            "name": "username",
            "comment": "用户名",
            "type": "REGULAR"
          },
          {
            "name": "email", 
            "comment": "邮箱地址",
            "type": "REGULAR"
          }, // JSON5支持尾随逗号
        ]
      }
    ],
    "domainEvents": [
      {
        "name": "UserRegisteredEvent",
        "comment": "用户注册事件"
      }
    ]
  }
]
```

然后使用 `JsonBasedDDDGenerator` 来生成：

```java
JsonBasedDDDGenerator generator = new JsonBasedDDDGenerator();
generator.generateFromJsonConfig("user-module.json5", "/path/to/project", 
    "com.example", false, "作者", "版权信息");
```

### 架构验证

基于ArchUnit的DDD架构约束检查：

- 分层依赖关系验证
- 聚合根设计原则检查
- 防腐层模式验证
- 命名约定验证

### 监控指标

内置完整的监控体系：

- 事件存储性能指标
- 命令执行监控
- 缓存命中率统计

## 📦 安装

### Gradle

```gradle
dependencies {
    implementation 'cn.treedeep:aggregatex-ddd-framework:1.0.1'
}
```

### Maven

```xml
<dependency>
    <groupId>cn.treedeep</groupId>
    <artifactId>aggregatex-ddd-framework</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 🚨 重要要求

**使用此框架的项目必须使用 `cn.treedeep.king` 作为基础包名**

项目结构应该类似：

```text
src/main/java/
└── cn/
    └── treedeep/
        └── king/
            └── your-project/
                ├── application/     # 应用层
                ├── domain/          # 领域层
                ├── infrastructure/  # 基础设施层
                └── presentation/    # 表现层
```

## 🏗️ 快速开始

### 1. 启用框架

```java
@SpringBootApplication
@EnableAggregateX
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 2. 配置

```yaml
# application.yml
app:
  event-store:
    type: jpa             # 事件存储类型：memory/jpa
    snapshot:
      enabled: true       # 是否启用快照
      frequency: 100      # 快照频率
  
  cqrs:
    async:
      enabled: true
    validation:
      enabled: true
  
  architecture:
    validation:
      enabled: true       # 是否启用架构验证
```

### 3. 使用代码生成器

**参考示例**:

查看 `examples/` 目录下的配置文件：

- `simple-product-module.json5` - 简单商品模块配置
- `module-config-with-remarks.json5` - 包含详细说明的模块配置

**编程式调用示例**:

```java
// 查看 src/test/java/cn/treedeep/king/test/DDDModuleGeneratorExample.java
// 了解完整的编程式使用方式

var userModule = ModuleInfo.create("user", "用户模块")
    .addAggregateRoot(
        AggregateRoot.create("User", "用户聚合根")
            .addProperty(Property.create("username", "用户名"))
            .addProperty(Property.create("email", "邮箱"))
    );

DDDModuleGenerator generator = new DDDModuleGenerator();
generator.generateModules("/path/to/project", "cn.treedeep.king.demo", 
    List.of(userModule), false);
```

### 4. 生成的模块结构

生成器会创建完整的DDD分层结构：

```text
src/main/java/cn/treedeep/king/user/
├── domain/                          # 领域层
│   ├── User.java                    # 聚合根
│   ├── UserId.java                  # 实体ID
│   ├── UserRepository.java         # 仓储接口
│   └── service/                     # 领域服务
├── application/                     # 应用层
│   ├── command/                     # 命令处理
│   ├── query/                       # 查询处理
│   ├── dto/                         # 数据传输对象
│   └── service/                     # 应用服务
├── infrastructure/                  # 基础设施层
│   ├── repository/                  # 仓储实现
│   └── ModuleConfig.java           # 模块配置
└── presentation/                    # 表现层
    └── UserController.java         # REST控制器
```

## 📚 最佳实践

### 聚合根设计

- 保持聚合边界清晰
- 使用强类型标识符
- 通过领域方法封装业务逻辑

### 事件设计

- 事件名称使用过去时
- 事件数据包含足够信息
- 事件应该是不可变的

## 🆕 版本历史

### v1.0.1 (2025-06) - 当前版本

- 智能代码生成器
- JSON5配置支持
- 模块文档生成
- 完整DDD分层代码生成

### v1.0.0 (2025-04)

- 实现核心DDD组件
- 提供基础设施支持

## 📄 许可证

本项目采用 [MPL 2.0 License](https://www.mozilla.org/en-US/MPL/2.0/) 许可证。
