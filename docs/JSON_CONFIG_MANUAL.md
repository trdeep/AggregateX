# DDD模块JSON配置系统使用手册

## 概述

DDD模块JSON配置系统是AggregateX框架的重要扩展功能，它允许用户通过JSON配置文件来定义DDD模块结构，并自动生成完整的DDD代码。这个系统提供了模块信息与JSON文件的双向转换功能，让DDD模块的配置和代码生成变得更加灵活和便捷。

## 主要特性

### 🔄 双向转换
- **模块对象 → JSON**: 将Java模块对象导出为JSON配置文件
- **JSON → 模块对象**: 从JSON配置文件加载模块对象
- **数据一致性**: 确保转换过程中数据完整性

### 📝 JSON配置支持
- **示例配置生成**: 自动生成示例JSON配置文件
- **配置验证**: 验证JSON配置文件格式的正确性
- **完整结构支持**: 支持聚合根、领域事件、应用服务等全部DDD概念

### 🏗️ 代码生成
- **从JSON生成**: 直接从JSON配置文件生成完整的DDD代码
- **灵活配置**: 支持项目路径、包名、作者等参数配置
- **覆盖控制**: 支持选择是否覆盖已存在的文件

## 核心类介绍

### 1. JsonBasedDDDGenerator
主要的JSON配置代码生成器，提供以下功能：
- `generateFromJsonConfig()`: 从JSON配置生成代码
- `createExampleConfig()`: 创建示例配置文件
- `validateJsonConfig()`: 验证配置文件
- `exportConfigToJson()`: 导出配置到JSON

### 2. ModuleConfigConverter
负责模块对象与JSON的双向转换：
- `modulesToJson()`: 模块对象转JSON字符串
- `jsonToModules()`: JSON字符串转模块对象
- `saveModulesToJsonFile()`: 保存模块到JSON文件
- `loadModulesFromJsonFile()`: 从JSON文件加载模块

### 3. ModuleConfigDto
JSON序列化的数据传输对象，包含：
- `ModuleConfigDto`: 模块配置DTO
- `AggregateRootDto`: 聚合根配置DTO
- `DomainEventDto`: 领域事件配置DTO
- `ApplicationServiceDto`: 应用服务配置DTO

### 4. DDDJsonConfigTool
便捷的工具类，简化常用操作：
- `createExampleConfig()`: 创建示例配置
- `generateFromConfig()`: 从配置生成代码
- `validateConfig()`: 验证配置
- `quickDemo()`: 快速生成演示项目

## JSON配置文件结构

### 基本结构
```json
[
  {
    "name": "模块名称",
    "comment": "模块注释",
    "aggregateRoots": [...],
    "domainEvents": [...],
    "applicationServices": [...]
  }
]
```

### 聚合根配置
```json
{
  "name": "User",
  "comment": "用户聚合根",
  "properties": [
    {
      "name": "username",
      "comment": "用户名",
      "type": "REGULAR"
    }
  ],
  "entities": [...],
  "valueObjects": [...],
  "methods": [...]
}
```

### 领域事件配置
```json
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
    }
  ]
}
```

### 应用服务配置
```json
{
  "name": "UserService",
  "comment": "用户服务",
  "interfaceName": "UserService",
  "implementationName": "UserServiceImpl",
  "methods": [
    {
      "name": "createUser",
      "comment": "创建用户",
      "returnType": "String",
      "parameters": [
        {
          "name": "username",
          "type": "String",
          "comment": "用户名"
        }
      ]
    }
  ]
}
```

## 使用方法

### 1. 快速开始

```java
// 创建工具实例
DDDJsonConfigTool tool = new DDDJsonConfigTool();

// 生成演示项目
tool.quickDemo("/tmp/ddd-demo", "my-project", "com.example.demo");
```

### 2. 创建示例配置

```java
DDDJsonConfigTool tool = new DDDJsonConfigTool();

// 创建示例配置文件
tool.createExampleConfig("/path/to/config.json");
```

### 3. 从Java对象导出配置

```java
// 创建模块对象
List<Module> modules = createYourModules();

// 导出为JSON配置
DDDJsonConfigTool tool = new DDDJsonConfigTool();
tool.exportModulesToJson(modules, "/path/to/output.json");
```

### 4. 从JSON配置生成代码

```java
DDDJsonConfigTool tool = new DDDJsonConfigTool();

// 从配置文件生成代码
tool.generateFromConfig(
    "/path/to/config.json",    // 配置文件路径
    "/path/to/project",        // 项目路径
    "com.example.project"      // 包名
);
```

### 5. 验证配置文件

```java
DDDJsonConfigTool tool = new DDDJsonConfigTool();

// 验证配置文件
boolean isValid = tool.validateConfig("/path/to/config.json");
if (isValid) {
    System.out.println("配置文件验证通过");
} else {
    System.out.println("配置文件格式错误");
}
```

### 6. 高级用法

```java
JsonBasedDDDGenerator generator = new JsonBasedDDDGenerator();
ModuleConfigConverter converter = generator.getConverter();

// 从JSON字符串加载模块
String jsonContent = "...";
List<Module> modules = converter.jsonToModules(jsonContent);

// 生成代码（完整参数）
generator.generateFromJsonConfig(
    "/path/to/config.json",
    "/path/to/project", 
    "com.example",
    true,                    // 是否覆盖
    "Your Name",            // 作者
    "Your Company"          // 版权
);
```

## 工作流程示例

### 1. 配置驱动的开发流程

```java
public class ConfigDrivenDevelopment {
    public static void main(String[] args) {
        DDDJsonConfigTool tool = new DDDJsonConfigTool();
        
        // Step 1: 创建示例配置
        tool.createExampleConfig("./config/modules.json");
        
        // Step 2: 手动编辑配置文件 (在IDE中修改)
        // 用户根据业务需求修改 ./config/modules.json
        
        // Step 3: 验证配置
        if (tool.validateConfig("./config/modules.json")) {
            // Step 4: 生成代码
            tool.generateFromConfig(
                "./config/modules.json",
                "./generated-project",
                "com.example.myproject"
            );
        }
    }
}
```

### 2. 代码到配置的流程

```java
public class CodeToConfigWorkflow {
    public static void main(String[] args) {
        DDDJsonConfigTool tool = new DDDJsonConfigTool();
        
        // Step 1: 使用Java代码定义模块
        List<Module> modules = createModulesInCode();
        
        // Step 2: 导出为JSON配置
        tool.exportModulesToJson(modules, "./exported-config.json");
        
        // Step 3: 基于导出的配置生成新项目
        tool.generateFromConfig(
            "./exported-config.json",
            "./new-project",
            "com.example.newproject"
        );
    }
}
```

## 最佳实践

### 1. 配置文件管理
- 将JSON配置文件放在版本控制中
- 为不同环境维护不同的配置文件
- 使用有意义的文件名和目录结构

### 2. 模块设计
- 保持聚合根的职责单一
- 合理设计领域事件的字段
- 应用服务方法应该体现业务用例

### 3. 代码生成
- 生成代码后，在生成的基础上进行业务逻辑开发
- 定期重新生成代码以保持结构一致性
- 将业务逻辑与生成的代码分离

### 4. 团队协作
- 统一JSON配置格式规范
- 建立配置评审流程
- 共享常用的配置模板

## 注意事项

### 1. 数据类型
- 属性类型使用Java标准类型名
- 参数类型支持基本类型和自定义类型
- 返回类型可以是void或任何Java类型

### 2. 命名规范
- 模块名使用小写下划线分隔
- 类名使用PascalCase
- 属性名和方法名使用camelCase

### 3. 文件覆盖
- 生成代码时注意是否覆盖已有文件
- 重要的业务逻辑代码要做好备份
- 建议使用版本控制系统

### 4. 性能考虑
- 大型项目建议分模块生成
- JSON文件不宜过大
- 生成的代码结构要合理

## 错误处理

系统提供了完善的错误处理机制：

```java
try {
    tool.generateFromConfig("config.json", "./project", "com.example");
} catch (RuntimeException e) {
    // 处理配置文件不存在、格式错误等异常
    System.err.println("生成失败: " + e.getMessage());
}
```

常见错误：
- 配置文件不存在
- JSON格式错误
- 模块名重复
- 包名格式错误
- 项目路径无权限

## 扩展性

JSON配置系统设计时考虑了扩展性：

1. **新增DDD概念**: 可以轻松添加新的DDD概念支持
2. **自定义模板**: 支持自定义代码生成模板
3. **插件机制**: 可以开发插件扩展功能
4. **格式支持**: 未来可能支持YAML等其他格式

## 总结

JSON配置系统为DDD模块开发提供了一种新的方式，它让配置和代码生成变得更加灵活。通过JSON配置，团队可以：

- 快速定义和修改DDD模块结构
- 保持代码生成的一致性
- 提高开发效率
- 降低学习成本
- 便于版本控制和团队协作

这个系统是对传统Java代码定义方式的重要补充，为不同的使用场景提供了合适的解决方案。
