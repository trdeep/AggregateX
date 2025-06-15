# DDD 模块 JSON 配置指南

## 概述

本文档描述了 AggregateX 框架的 JSON 配置格式，用于定义 DDD（领域驱动设计）模块结构。通过 JSON 配置，可以快速生成完整的 DDD 代码结构。

## 配置结构

JSON 配置是一个模块数组，每个模块包含以下主要部分：

```json
[
  {
    "name": "模块名称",
    "comment": "模块描述",
    "aggregateRoots": [聚合根配置],
    "domainEvents": [领域事件配置],
    "applicationServices": [应用服务配置]
  }
]
```

## 核心概念说明

### 属性类型 (PropertyDto.type)

每个属性必须指定类型，用于确定代码生成方式：

| 类型 | 值 | 用途 | 生成结果 |
|-----|-----|------|----------|
| **普通属性** | `"REGULAR"` | 基础业务字段 | 普通属性字段 |
| **值对象属性** | `"VALUE_OBJECT_PROPERTY"` | 实体中嵌套值对象 | `@Embedded` 注解 |
| **聚合根属性** | `"AGGREGATE_ROOT_PROPERTY"` | 聚合根中嵌套值对象 | `@Embedded` 注解 |

## 完整配置示例

```json
[
  {
    "name": "user",
    "comment": "用户模块",
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
            "comment": "邮箱",
            "type": "REGULAR"
          },
          {
            "name": "phone",
            "comment": "手机号",
            "type": "AGGREGATE_ROOT_PROPERTY"
          }
        ],
        "entities": [
          {
            "name": "LoginRecord",
            "comment": "登录记录",
            "properties": [
              {
                "name": "loginTime",
                "comment": "登录时间",
                "type": "REGULAR"
              },
              {
                "name": "ipAddress",
                "comment": "IP地址",
                "type": "REGULAR"
              },
              {
                "name": "deviceFingerprint",
                "comment": "设备指纹",
                "type": "VALUE_OBJECT_PROPERTY"
              }
            ]
          }
        ],
        "valueObjects": [
          {
            "name": "Phone",
            "comment": "手机号值对象",
            "properties": [
              {
                "name": "phoneNumber",
                "comment": "手机号码",
                "type": "REGULAR"
              },
              {
                "name": "countryCode",
                "comment": "国家代码",
                "type": "REGULAR"
              }
            ]
          },
          {
            "name": "DeviceFingerprint",
            "comment": "设备指纹值对象",
            "properties": [
              {
                "name": "browserType",
                "comment": "浏览器类型",
                "type": "REGULAR"
              },
              {
                "name": "deviceId",
                "comment": "设备ID",
                "type": "REGULAR"
              }
            ]
          }
        ],
        "methods": [
          {
            "name": "login",
            "comment": "用户登录",
            "returnType": "boolean",
            "parameters": [
              {
                "name": "password",
                "type": "String",
                "comment": "密码"
              }
            ]
          },
          {
            "name": "logout",
            "comment": "用户登出",
            "returnType": "void",
            "parameters": []
          }
        ]
      }
    ],
    "domainEvents": [
      {
        "name": "UserRegisteredEvent",
        "comment": "用户注册事件",
        "aggregateRootName": "User",
        "tableName": "user_registered_events",
        "fields": [
          {
            "name": "userId",
            "type": "String",
            "comment": "用户ID",
            "columnName": "user_id"
          },
          {
            "name": "username",
            "type": "String",
            "comment": "用户名",
            "columnName": "username"
          },
          {
            "name": "email",
            "type": "String",
            "comment": "邮箱",
            "columnName": "email"
          }
        ]
      }
    ],
    "applicationServices": [
      {
        "name": "UserApplicationService",
        "comment": "用户应用服务",
        "moduleName": "user",
        "interfaceName": "UserApplicationService",
        "implementationName": "UserApplicationServiceImpl",
        "methods": [
          {
            "name": "registerUser",
            "comment": "注册用户",
            "returnType": "void",
            "parameters": [
              {
                "name": "command",
                "type": "RegisterUserCommand",
                "comment": "注册用户命令"
              }
            ]
          }
        ]
      }
    ]
  }
]
```

## 配置规则

### 1. 命名规范
- **模块名**: 小写字母，使用下划线分隔 (`user`, `order_management`)
- **类名**: 大驼峰命名 (`User`, `LoginRecord`, `DeviceFingerprint`)
- **属性名**: 小驼峰命名 (`username`, `phoneNumber`, `loginTime`)
- **方法名**: 小驼峰命名 (`login`, `registerUser`)

### 2. 属性类型选择指南

#### REGULAR (普通属性)
- **使用场景**: 基础数据字段
- **例子**: `username`, `email`, `status`, `createTime`
- **生成代码**: 
  ```java
  @Comment("用户名")
  private String username;
  ```

#### VALUE_OBJECT_PROPERTY (值对象属性)
- **使用场景**: 实体中需要嵌套值对象
- **例子**: 实体中的 `deviceFingerprint`, `address`
- **生成代码**:
  ```java
  @Embedded
  @Comment("设备指纹")
  private DeviceFingerprint deviceFingerprint;
  ```

#### AGGREGATE_ROOT_PROPERTY (聚合根属性)
- **使用场景**: 聚合根中需要嵌套值对象
- **例子**: 聚合根中的 `phone`, `address`
- **生成代码**:
  ```java
  @Embedded
  @Comment("手机号")
  private Phone phone;
  ```

### 3. 数据类型映射

| JSON type | Java类型 | 说明 |
|-----------|----------|------|
| `"String"` | `String` | 字符串类型 |
| `"boolean"` | `boolean` | 布尔类型 |
| `"int"` | `int` | 整数类型 |
| `"long"` | `long` | 长整数类型 |
| `"void"` | `void` | 无返回值 |

### 4. 时间字段自动识别

包含以下关键词的属性会自动生成为 `OffsetDateTime` 类型：
- `Time` (如 `loginTime`, `createTime`)
- `Date` (如 `birthDate`, `expireDate`)
- `At` (如 `createdAt`, `updatedAt`)

## 最佳实践

### 1. 聚合设计
- 一个聚合根应该包含相关的实体和值对象
- 聚合根的属性使用 `AGGREGATE_ROOT_PROPERTY` 嵌入值对象
- 实体的属性使用 `VALUE_OBJECT_PROPERTY` 嵌入值对象

### 2. 值对象设计
- 值对象应该是不可变的
- 包含相关的属性组合 (如手机号 = 号码 + 国家代码)
- 名称应该体现业务含义

### 3. 事件设计
- 事件名使用过去时态 (如 `UserRegisteredEvent`)
- 包含必要的业务数据
- 表名使用下划线命名

### 4. 应用服务设计
- 服务名以 `ApplicationService` 结尾
- 方法参数使用命令对象
- 方法名体现业务操作

## 验证清单

在创建 JSON 配置时，请检查：

- [ ] 所有名称符合命名规范
- [ ] 属性类型正确选择 (`REGULAR`, `VALUE_OBJECT_PROPERTY`, `AGGREGATE_ROOT_PROPERTY`)
- [ ] 值对象在对应的 `valueObjects` 数组中定义
- [ ] 领域事件包含必要的字段
- [ ] 应用服务方法定义完整

## 生成文件预览

基于上述配置，将生成以下文件结构：

```
src/main/java/com/example/user/
├── domain/
│   ├── User.java                    # 聚合根
│   ├── UserId.java                  # 聚合根ID
│   ├── LoginRecord.java             # 实体
│   ├── Phone.java                   # 值对象
│   ├── DeviceFingerprint.java       # 值对象
│   └── event/
│       └── UserRegisteredEvent.java # 领域事件
├── application/
│   ├── UserApplicationService.java  # 应用服务接口
│   └── UserApplicationServiceImpl.java # 应用服务实现
└── infrastructure/
    ├── UserRepository.java          # 仓储接口
    ├── UserRepositoryImpl.java      # 仓储实现
    └── UserJpaRepository.java       # JPA仓储
```

## ⚠️ 重要限制说明

### 方法参数和返回值类型限制

**生成器对象类型限制**: 当方法的 `returnType` 或 `parameters` 中的 `type` 是自定义对象类型时，AggregateX 生成器**不会自动生成这些对象类**。这些类需要您手动创建。

#### 影响范围
- 领域方法 (`methods`) 的返回类型
- 领域方法参数的类型
- 应用服务方法的返回类型  
- 应用服务方法参数的类型

#### 示例说明

```json
{
  "methods": [
    {
      "name": "processOrder",
      "comment": "处理订单",
      "returnType": "OrderResult",        // ❌ 需要手动创建 OrderResult 类
      "parameters": [
        {
          "name": "command",
          "type": "ProcessOrderCommand",  // ❌ 需要手动创建 ProcessOrderCommand 类  
          "comment": "处理订单命令"
        }
      ]
    }
  ]
}
```

#### 解决方案

需要AI或开发者按照 AggregateX 规范手动创建这些类：

**1. 命令对象 (Command)**
```java
// ProcessOrderCommand.java
package com.example.order.application.command;

import cn.treedeep.king.core.application.cqrs.command.Command;
import lombok.Getter;

/**
 * 处理订单命令
 */
@Getter
public class ProcessOrderCommand extends Command {
    
    /**
     * 订单ID
     */
    private final String orderId;
    
    /**
     * 处理类型
     */
    private final String processType;
    
    /**
     * 处理参数
     */
    private final String processParams;
    
    public ProcessOrderCommand(String orderId, String processType, String processParams) {
        this.orderId = orderId;
        this.processType = processType;
        this.processParams = processParams;
    }
    
    @Override
    public String getAggregateId() {
        return orderId;
    }
}
```

**2. 结果对象 (Result)**
```java
// OrderResult.java
package com.example.order.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 订单处理结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResult {
    
    /**
     * 处理是否成功
     */
    private boolean success;
    
    /**
     * 结果消息
     */
    private String message;
    
    /**
     * 结果数据
     */
    private Object data;
}
```

**3. 查询对象 (Query)**
```java
// UserQuery.java
package com.example.user.application.query;

import com.example.user.application.query.result.UserListQueryResult;
import cn.treedeep.king.core.application.cqrs.query.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户查询
 */
@AllArgsConstructor
public class UserQuery extends Query<UserListQueryResult> {
    
    /**
     * 用户名
     */
    @Getter
    private String username;
    
    /**
     * 邮箱
     */
    @Getter
    private String email;
    
    /**
     * 状态
     */
    @Getter
    private String status;
    
    /**
     * 页码
     */
    @Getter
    private int page = 0;
    
    /**
     * 页大小
     */
    @Getter
    private int size = 20;
    
    @Override
    public String getQueryName() {
        return "UserQuery";
    }
}
```

**4. 查询结果对象 (QueryResult)**
```java
// UserListQueryResult.java
package com.example.user.application.query.result;

import com.example.user.application.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 用户列表查询结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListQueryResult {
    
    /**
     * 用户列表
     */
    private List<UserDto> users;
    
    /**
     * 总数量
     */
    private long total;
    
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 页大小
     */
    private int size;
}
```

#### 推荐的包结构

```
src/main/java/包名/模块名/
├── application/
│   ├── command/           # 命令对象
│   │   ├── CreateUserCommand.java
│   │   └── UpdateUserCommand.java
│   ├── query/             # 查询对象
│   │   ├── UserQuery.java
│   │   └── result/        # 查询结果对象
│   │       ├── UserListQueryResult.java
│   │       └── UserDetailQueryResult.java
│   └── dto/               # 数据传输对象
│       └── UserDto.java
├── domain/
│   └── model/             # 领域模型结果对象
│       └── UserResult.java
```

#### 最佳实践建议

1. **命令对象**: 用于封装应用服务方法的输入参数，继承框架 Command 基类
2. **查询对象**: 用于封装查询条件和分页信息，继承框架 Query 基类
3. **结果对象**: 用于封装方法返回的业务数据，放在 dto 包中
4. **查询结果对象**: 专门用于查询操作的结果，放在 query/result 包中
5. **DTO对象**: 用于应用层和外部的数据传输，使用 Lombok 注解简化代码

## 注意事项

1. **属性引用**: 当使用 `VALUE_OBJECT_PROPERTY` 或 `AGGREGATE_ROOT_PROPERTY` 时，确保对应的值对象在 `valueObjects` 中定义
2. **循环依赖**: 避免值对象之间的循环引用
3. **命名冲突**: 确保同一模块内的类名不重复
4. **JSON格式**: 确保 JSON 格式正确，所有字段都有值
5. **⚠️ 对象类型**: 方法中使用的自定义对象类型需要手动创建，生成器不会自动生成

通过遵循这个配置指南，可以确保生成的代码结构清晰、符合 DDD 设计原则，并且易于维护。
