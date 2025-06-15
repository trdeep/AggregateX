package cn.treedeep.king.generator.config;

import cn.treedeep.king.generator.DDDModuleGenerator;
import cn.treedeep.king.generator.model.Module;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 基于JSON配置的DDD模块生成器
 * <p>
 * 通过读取JSON配置文件来生成DDD模块代码
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Slf4j
public class JsonBasedDDDGenerator {

    private final ModuleConfigConverter converter;
    private final DDDModuleGenerator generator;

    public JsonBasedDDDGenerator() {
        this.converter = new ModuleConfigConverter();
        this.generator = new DDDModuleGenerator();
    }

    /**
     * 从JSON配置文件生成代码
     *
     * @param configFilePath 配置文件路径
     * @param projectPath 项目路径
     * @param packageName 包名
     * @param isCover 是否覆盖
     * @param author 作者
     * @param copyright 版权信息
     */
    public void generateFromJsonConfig(String configFilePath, String projectPath, String packageName, 
                                     boolean isCover, String author, String copyright) {
        try {
            log.info("🔧 开始从JSON配置生成DDD模块...");
            log.info("📁 配置文件: {}", configFilePath);
            log.info("📁 项目路径: {}", projectPath);
            log.info("📦 包名: {}", packageName);

            // 验证配置文件存在
            Path configPath = Paths.get(configFilePath);
            if (!Files.exists(configPath)) {
                throw new RuntimeException("配置文件不存在: " + configFilePath);
            }

            // 从JSON文件加载模块配置
            List<Module> modules = converter.loadModulesFromJsonFile(configFilePath);
            log.info("📋 加载了 {} 个模块配置", modules.size());

            // 打印模块信息
            modules.forEach(module -> {
                log.info("📦 模块: {} - {}", module.getName(), module.getComment());
                log.info("  🏗️ 聚合根: {}", module.getAggregateRoots().size());
                log.info("  📨 领域事件: {}", module.getDomainEvents().size());
                log.info("  🔧 应用服务: {}", module.getApplicationServices().size());
            });

            // 生成代码
            generator.generateModules(projectPath, packageName, modules, isCover, author, copyright);
            
            log.info("✅ JSON配置生成完成！");

        } catch (Exception e) {
            log.error("❌ JSON配置生成失败", e);
            throw new RuntimeException("JSON配置生成失败", e);
        }
    }

    /**
     * 从JSON配置文件生成代码（使用默认参数）
     *
     * @param configFilePath 配置文件路径
     * @param projectPath 项目路径
     * @param packageName 包名
     */
    public void generateFromJsonConfig(String configFilePath, String projectPath, String packageName) {
        generateFromJsonConfig(configFilePath, projectPath, packageName, true, "System", "深圳市树深计算机系统有限公司");
    }

    /**
     * 将当前代码配置导出为JSON文件（用于创建模板）
     *
     * @param modules 模块列表
     * @param outputFilePath 输出文件路径
     */
    public void exportConfigToJson(List<Module> modules, String outputFilePath) {
        try {
            converter.saveModulesToJsonFile(modules, outputFilePath);
            log.info("✅ 配置已导出到: {}", outputFilePath);
        } catch (Exception e) {
            log.error("❌ 导出配置失败", e);
            throw new RuntimeException("导出配置失败", e);
        }
    }

    /**
     * 验证JSON配置文件格式
     *
     * @param configFilePath 配置文件路径
     * @return 验证结果
     */
    public boolean validateJsonConfig(String configFilePath) {
        try {
            List<Module> modules = converter.loadModulesFromJsonFile(configFilePath);
            log.info("✅ 配置文件格式验证通过: {}", configFilePath);
            log.info("📋 包含 {} 个模块", modules.size());
            return true;
        } catch (Exception e) {
            log.error("❌ 配置文件格式验证失败: {}", configFilePath, e);
            return false;
        }
    }

    /**
     * 创建示例JSON配置文件
     *
     * @param outputFilePath 输出文件路径
     */
    public void createExampleConfig(String outputFilePath) {
        try {
            log.info("🔧 创建示例配置文件...");
            
            // 这里可以创建一个示例配置，或者从现有的示例生成
            String exampleJson = """
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
                          }
                        ],
                        "entities": [],
                        "valueObjects": [],
                        "methods": [
                          {
                            "name": "register",
                            "comment": "用户注册",
                            "returnType": "void",
                            "parameters": [
                              {
                                "name": "username",
                                "type": "String",
                                "comment": "用户名"
                              },
                              {
                                "name": "email",
                                "type": "String",
                                "comment": "邮箱"
                              }
                            ]
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
                          }
                        ]
                      }
                    ],
                    "applicationServices": [
                      {
                        "name": "UserService",
                        "comment": "用户服务",
                        "moduleName": null,
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
                    ]
                  }
                ]
                """;
            
            Path path = Paths.get(outputFilePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            Files.writeString(path, exampleJson);
            log.info("✅ 示例配置文件已创建: {}", outputFilePath);
            
        } catch (Exception e) {
            log.error("❌ 创建示例配置文件失败", e);
            throw new RuntimeException("创建示例配置文件失败", e);
        }
    }

    /**
     * 获取转换器实例
     */
    public ModuleConfigConverter getConverter() {
        return converter;
    }

    /**
     * 获取生成器实例
     */
    public DDDModuleGenerator getGenerator() {
        return generator;
    }
}
