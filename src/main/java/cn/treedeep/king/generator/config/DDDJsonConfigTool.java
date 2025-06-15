package cn.treedeep.king.generator.config;

import cn.treedeep.king.generator.model.Module;
import cn.treedeep.king.shared.utils.Json5Parser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * DDD模块JSON配置工具
 * <p>
 * 提供便捷的JSON配置文件操作方法，简化用户使用流程
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Slf4j
public class DDDJsonConfigTool {

    private final JsonBasedDDDGenerator jsonGenerator;
    private final ModuleConfigConverter converter;

    public DDDJsonConfigTool() {
        this.jsonGenerator = new JsonBasedDDDGenerator();
        this.converter = jsonGenerator.getConverter();
    }

    /**
     * 创建示例配置文件
     * <p>
     * 在指定路径创建一个示例JSON配置文件，用户可以基于此文件进行修改
     *
     * @param outputPath 输出路径
     */
    public void createExampleConfig(String outputPath) {
        log.info("🔧 创建示例配置文件: {}", outputPath);
        jsonGenerator.createExampleConfig(outputPath);

        printUsageInstructions(outputPath);
    }

    /**
     * 从Java模块对象导出JSON配置
     * <p>
     * 将现有的Module对象列表导出为JSON配置文件
     *
     * @param modules 模块列表
     * @param outputPath 输出路径
     */
    public void exportModulesToJson(List<Module> modules, String outputPath) {
        log.info("📤 导出模块配置到JSON文件: {}", outputPath);
        jsonGenerator.exportConfigToJson(modules, outputPath);

        log.info("💡 提示: 你可以编辑这个JSON文件来修改模块配置");
    }

    /**
     * 验证JSON配置文件
     * <p>
     * 检查JSON配置文件的格式是否正确
     *
     * @param configPath 配置文件路径
     * @return 是否验证通过
     */
    public boolean validateConfig(String configPath) {
        log.info("✅ 验证配置文件: {}", configPath);
        return jsonGenerator.validateJsonConfig(configPath);
    }

    /**
     * 从JSON配置文件生成DDD代码
     * <p>
     * 这是主要的代码生成方法，从JSON配置文件生成完整的DDD模块代码
     *
     * @param configPath 配置文件路径
     * @param projectPath 项目路径
     * @param packageName 包名
     */
    public void generateFromConfig(String configPath, String projectPath, String packageName) {
        generateFromConfig(configPath, projectPath, packageName, true, "System", "深圳市树深计算机系统有限公司");
    }

    /**
     * 从JSON配置文件生成DDD代码（完整参数）
     *
     * @param configPath 配置文件路径
     * @param projectPath 项目路径
     * @param packageName 包名
     * @param isCover 是否覆盖已存在的文件
     * @param author 作者
     * @param copyright 版权信息
     */
    public void generateFromConfig(String configPath, String projectPath, String packageName,
                                 boolean isCover, String author, String copyright) {
        log.info("🏗️ 从JSON配置生成DDD代码");
        log.info("📁 配置文件: {}", configPath);
        log.info("📁 项目路径: {}", projectPath);
        log.info("📦 包名: {}", packageName);

        jsonGenerator.generateFromJsonConfig(configPath, projectPath, packageName, isCover, author, copyright);

        printPostGenerationInstructions(projectPath);
    }

    /**
     * 从JSON字符串生成DDD代码
     * <p>
     * 直接从JSON字符串生成代码，不需要文件
     * 支持JSON5格式（包含注释和尾随逗号）
     *
     * @param jsonContent JSON内容（支持JSON5格式）
     * @param projectPath 项目路径
     * @param packageName 包名
     */
    public void generateFromJsonString(String jsonContent, String projectPath, String packageName) {
        log.info("🏗️ 从JSON字符串生成DDD代码");

        try {
            // 检测是否为JSON5格式并转换为标准JSON
            String processedJson;
            if (Json5Parser.isJson5(jsonContent)) {
                log.info("📝 检测到JSON5格式，正在转换为标准JSON...");
                processedJson = Json5Parser.convertJson5ToJson(jsonContent);
                log.info("✅ JSON5转换完成");
            } else {
                log.info("📄 使用标准JSON格式");
                processedJson = jsonContent;
            }

            // 解析JSON为模块对象
            List<Module> modules = converter.jsonToModules(processedJson);

            // 生成代码
            jsonGenerator.getGenerator().generateModules(projectPath, packageName, modules, true);

            log.info("✅ 代码生成完成");
            printPostGenerationInstructions(projectPath);

        } catch (Exception e) {
            log.error("❌ 从JSON字符串生成代码失败", e);
            throw new RuntimeException("从JSON字符串生成代码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从JSON配置文件加载模块
     * <p>
     * 将JSON配置文件解析为Java模块对象
     *
     * @param configPath 配置文件路径
     * @return 模块列表
     */
    public List<Module> loadModulesFromConfig(String configPath) {
        log.info("📋 从配置文件加载模块: {}", configPath);
        return converter.loadModulesFromJsonFile(configPath);
    }

    /**
     * 将模块对象转换为JSON字符串
     * <p>
     * 便于查看或调试模块配置
     *
     * @param modules 模块列表
     * @return JSON字符串
     */
    public String modulesToJsonString(List<Module> modules) {
        return converter.modulesToJson(modules);
    }

    /**
     * 获取转换器实例
     */
    public ModuleConfigConverter getConverter() {
        return converter;
    }

    /**
     * 获取JSON生成器实例
     */
    public JsonBasedDDDGenerator getJsonGenerator() {
        return jsonGenerator;
    }

    /**
     * 打印使用说明
     */
    private void printUsageInstructions(String configPath) {
        log.info("\n📖 使用说明:");
        log.info("1. 编辑配置文件: {}", configPath);
        log.info("2. 根据你的业务需求修改模块、聚合根、事件和服务配置");
        log.info("3. 使用 generateFromConfig() 方法生成代码");
        log.info("\n💡 配置文件包含以下主要部分:");
        log.info("   • aggregateRoots: 聚合根定义");
        log.info("   • domainEvents: 领域事件定义");
        log.info("   • applicationServices: 应用服务定义");
        log.info("\n🔗 更多信息请参考项目文档");
    }

    /**
     * 打印代码生成后的说明
     */
    private void printPostGenerationInstructions(String projectPath) {
        log.info("\n🎉 代码生成完成!");
        log.info("📁 项目位置: {}", projectPath);
        log.info("\n💡 后续步骤:");
        log.info("1. 导入项目到IDE中");
        log.info("2. 根据业务需求完善领域逻辑");
        log.info("3. 实现仓储接口");
        log.info("4. 添加单元测试");
        log.info("5. 配置数据库连接");
        log.info("\n🔧 相关命令:");
        log.info("   • 编译项目: ./gradlew build");
        log.info("   • 运行测试: ./gradlew test");
    }

    /**
     * 快速生成演示
     * <p>
     * 一键生成包含示例配置和代码的完整演示
     *
     * @param demoPath 演示路径
     * @param projectName 项目名称
     * @param packageName 包名
     */
    public void quickDemo(String demoPath, String projectName, String packageName) {
        log.info("🚀 快速生成DDD演示项目");

        String configPath = demoPath + "/" + projectName + "-config.json";
        String projectPath = demoPath + "/" + projectName;

        // 1. 创建示例配置
        createExampleConfig(configPath);

        // 2. 生成代码
        generateFromConfig(configPath, projectPath, packageName);

        log.info("\n✨ 演示项目生成完成!");
        log.info("📂 配置文件: {}", configPath);
        log.info("📂 项目代码: {}", projectPath);
        log.info("\n🎯 你可以:");
        log.info("1. 查看生成的代码结构");
        log.info("2. 修改配置文件并重新生成");
        log.info("3. 基于此开始你的DDD项目开发");
    }
}
