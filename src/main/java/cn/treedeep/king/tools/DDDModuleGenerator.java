package cn.treedeep.king.tools;

import cn.treedeep.king.tools.model.EntityInfo;
import cn.treedeep.king.tools.model.ModuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * DDD模块生成器
 * <p>
 * 用于根据AggregateX框架的DDD规则生成标准的模块包结构
 * <p>
 * 使用方法:<br>
 * 1. 作为Spring Boot应用运行: {@code java -jar generator.jar}<br>
 * 2. 作为命令行工具: {@code java -cp generator.jar cn.treedeep.king.tools.generator.DDDModuleGenerator}
 *
 * @author AggregateX DDD Framework
 * @since 1.0.0
 */
@Slf4j
public class DDDModuleGenerator {

    private static final String BASE_PACKAGE_PATH = "cn/treedeep/king";

    /**
     * 生成DDD模块
     */
    public void generateModule(String projectPath,
                               String packageName,
                               boolean isCover,
                               String moduleName,
                               String moduleComment,
                               String copyright,
                               String author) throws IOException {
        printBanner();

        log.info("🏗️ 工程目录：{}", projectPath);

        if (StringUtils.isBlank(copyright)) {
            copyright = "深圳市树深计算机系统有限公司";
        }
        if (StringUtils.isBlank(author)) {
            author = "AggregateX";
        }

        // 处理包名，如果为空则使用默认包路径
        String actualPackageName;
        String actualPackagePath;
        if (StringUtils.isBlank(packageName)) {
            actualPackageName = BASE_PACKAGE_PATH.replace("/", ".");
            actualPackagePath = BASE_PACKAGE_PATH;
        } else {
            actualPackageName = packageName;
            actualPackagePath = packageName.replace(".", "/");
        }

        validateInputs(projectPath, moduleName.toLowerCase());

        Path javaSourcePath = determineJavaSourcePath(projectPath, actualPackagePath);
        Path modulePath = javaSourcePath.resolve(moduleName);

        // 检查模块是否已存在
        boolean shouldOverwrite = isCover;
        if (Files.exists(modulePath) && !isCover) {
            log.warn("⚠️ 模块 '{}' 已存在于路径: {}", moduleName, modulePath);
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("是否要覆盖现有模块文件? (y/N): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!"y".equals(response) && !"yes".equals(response)) {
                    log.info("操作已取消");
                    return;
                }
                shouldOverwrite = true;
            }
        }

        if (Files.exists(modulePath) && shouldOverwrite) {
            log.info("📝 将覆盖现有模块文件...");
        }

        log.info("📁 创建目录结构...");
        createDirectoryStructure(modulePath);

        log.info("📝 生成模板文件...");
        generateTemplateFiles(modulePath, actualPackageName, moduleName, moduleComment, copyright, author);

        log.info("✅ 模块 '{}' 生成完成", moduleName);
        log.info("📍 模块位置: {}", modulePath.toAbsolutePath());

        printNextSteps();
    }

    /**
     * 验证输入参数
     */
    private void validateInputs(String projectPath, String moduleName) {
        Path path = Paths.get(projectPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new IllegalArgumentException("项目路径不存在或不是目录: " + projectPath);
        }

        if (!moduleName.matches("^[a-zA-Z][a-zA-Z0-9_-]*$")) {
            throw new IllegalArgumentException("模块名称格式不正确，只能包含字母、数字、下划线和连字符，且必须以字母开头");
        }
    }

    /**
     * 确定Java源码路径
     */
    private Path determineJavaSourcePath(String projectPath, String packagePath) {
        Path projectDir = Paths.get(projectPath);

        // 优先检查标准路径
        Path standardPath = projectDir.resolve("src/main/java").resolve(packagePath);
        if (Files.exists(standardPath)) {
            return standardPath;
        }

        // 检查example路径
        Path examplePath = projectDir.resolve("example/src/main/java").resolve(packagePath);
        if (Files.exists(examplePath)) {
            return examplePath;
        }

        // 如果都不存在，创建标准路径
        try {
            Files.createDirectories(standardPath);
            return standardPath;
        } catch (IOException e) {
            throw new RuntimeException("无法创建Java源码目录: " + standardPath, e);
        }
    }

    /**
     * 创建目录结构
     */
    private void createDirectoryStructure(Path modulePath) throws IOException {
        // 领域层目录
        Files.createDirectories(modulePath.resolve("domain/event"));
        Files.createDirectories(modulePath.resolve("domain/service"));

        // 应用层目录
        Files.createDirectories(modulePath.resolve("application/command"));
        Files.createDirectories(modulePath.resolve("application/query"));
        Files.createDirectories(modulePath.resolve("application/dto"));
        Files.createDirectories(modulePath.resolve("application/service"));

        // 基础设施层目录
        Files.createDirectories(modulePath.resolve("infrastructure/repository"));
        Files.createDirectories(modulePath.resolve("infrastructure/service"));

        // 表现层目录
        Files.createDirectories(modulePath.resolve("presentation/dto"));

        // 接口层目录
        Files.createDirectories(modulePath.resolve("interfaces"));
    }

    /**
     * 生成模板文件
     */
    private void generateTemplateFiles(Path modulePath, String packageName, String moduleName, String moduleComment, String copyright, String author) throws IOException {
        String entityNameCamel = toPascalCase(moduleName);
        String moduleNameLower = moduleName.toLowerCase();

        if (moduleComment.isEmpty()) {
            moduleComment = entityNameCamel;
        }

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(modulePath, packageName, entityNameCamel, moduleNameLower, moduleComment, copyright, author);

        // 生成领域层文件
        templateGenerator.generateDomainFiles();

        // 生成应用层文件
        templateGenerator.generateApplicationFiles();

        // 生成基础设施层文件
        templateGenerator.generateInfrastructureFiles();

        // 生成表现层文件
        templateGenerator.generatePresentationFiles();

        // 生成包信息文件
        templateGenerator.generatePackageInfoFiles();

        // 生成 README.md 文件
        templateGenerator.generateReadmeFiles();
    }

    /**
     * 转换为PascalCase格式
     */
    private String toPascalCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    /**
     * 递归删除目录
     */
    @SuppressWarnings("unused")
    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder()) // 先删除子目录
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            log.warn("删除文件失败: {}", p, e);
                        }
                    });
        }
    }

    /**
     * 打印横幅
     */
    private void printBanner() {
        System.out.println();
        System.out.println("    ___                                  __      _  __");
        System.out.println("   /   |  ____ _____ ________  ____ _____ _/ /____ | |/ /");
        System.out.println("  / /| | / __ `/ __ `/ ___/ _ \\/ __ `/ __ `/ __/ _ \\|   / ");
        System.out.println(" / ___ |/ /_/ / /_/ / /  /  __/ /_/ / /_/ / /_/  __/   |  ");
        System.out.println("/_/  |_|\\__, /\\__, /_/   \\___/\\__, /\\__,_/\\__/\\___/_/|_|  ");
        System.out.println("       /____//____/         /____/                      ");
        System.out.println();
        System.out.println("DDD Module Generator v1.0.0");
        System.out.println("Generated at: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println();
    }

    /**
     * 打印后续步骤建议
     */
    private void printNextSteps() {
        System.out.println();
        System.out.println("💡 下一步操作建议:");
        System.out.println("   1. 根据具体业务需求调整聚合根和值对象");
        System.out.println("   2. 完善领域事件和业务规则");
        System.out.println("   3. 添加单元测试和集成测试");
        System.out.println("   4. 配置数据库迁移脚本");
        System.out.println("   5. 更新API文档");
        System.out.println();
        System.out.println("🔧 相关命令:");
        System.out.println("   • 编译项目: ./gradlew build");
        System.out.println("   • 运行测试: ./gradlew test");
        System.out.println("   • 生成文档: ./gradlew javadoc");
        System.out.println();
    }

    /**
     * 批量生成DDD模块 - 新的使用方式
     * 支持基于 ModuleInfo 和 EntityInfo 的模块生成
     *
     * @param projectPath 项目路径
     * @param packageName 包名
     * @param modules 模块信息列表
     * @param author 作者
     * @param copyright 版权信息
     */
    public void generateModules(String projectPath,
                               String packageName,
                               List<ModuleInfo> modules,
                               String author,
                               String copyright) {

        log.info("🏗️ 开始批量生成模块...");
        log.info("📁 工程目录：{}", projectPath);
        log.info("📦 将生成 {} 个模块", modules.size());

        for (int i = 0; i < modules.size(); i++) {
            ModuleInfo moduleInfo = modules.get(i);
            String moduleName = moduleInfo.getName();
            String moduleComment = moduleInfo.getComment();
            List<EntityInfo> entities = moduleInfo.getEntities();

            try {
                log.info("📝 正在生成模块 [{}/{}]: {} - {}", i + 1, modules.size(), moduleName, moduleComment);

                // 为每个模块生成基础结构
                generateModuleWithEntities(projectPath, packageName, true, moduleName, moduleComment,
                                         entities, copyright, author);

                log.info("✅ 模块 '{}' 生成完成", moduleName);

            } catch (Exception e) {
                log.error("❌ 模块 '{}' 生成失败: {}", moduleName, e.getMessage(), e);
            }
        }

        log.info("🎉 批量生成完成！");
    }

    /**
     * 生成带多个实体的DDD模块
     */
    private void generateModuleWithEntities(String projectPath,
                                          String packageName,
                                          boolean isCover,
                                          String moduleName,
                                          String moduleComment,
                                          List<EntityInfo> entities,
                                          String copyright,
                                          String author) throws IOException {

        // 模块信息（moduleName, moduleComment）不生成代码，只作为模块容器
        // 真正的代码生成基于 EntityInfo 列表，同一模块下的所有实体在同一个DDD分层目录中

        if (entities != null && !entities.isEmpty()) {
            log.info("📋 模块 '{}' 包含 {} 个实体", moduleName, entities.size());

            // 创建模块基础目录结构
            String actualPackageName;
            String actualPackagePath;
            if (StringUtils.isBlank(packageName)) {
                actualPackageName = BASE_PACKAGE_PATH.replace("/", ".");
                actualPackagePath = BASE_PACKAGE_PATH;
            } else {
                actualPackageName = packageName;
                actualPackagePath = packageName.replace(".", "/");
            }

            Path javaSourcePath = determineJavaSourcePath(projectPath, actualPackagePath);
            Path modulePath = javaSourcePath.resolve(moduleName);

            // 创建目录结构
            log.info("📁 创建模块目录结构...");
            createDirectoryStructure(modulePath);

            // 为每个实体生成代码文件，但都在同一个模块目录下
            for (EntityInfo entityInfo : entities) {
                String entityName = entityInfo.getName();
                String entityComment = entityInfo.getComment();

                log.info("  🔸 生成实体: {} - {}", entityName, entityComment);

                // 生成实体相关的文件
                generateEntityFiles(modulePath, actualPackageName, moduleName, entityName, entityComment, copyright, author);
            }

            // 生成模块级的配置和包信息文件
            generateModuleLevelFiles(modulePath, actualPackageName, moduleName, moduleComment, copyright, author);

        } else {
            // 如果没有实体信息，使用传统方式生成（向后兼容）
            generateModule(projectPath, packageName, isCover, moduleName, moduleComment, copyright, author);
        }
    }

    /**
     * 为单个实体生成文件
     */
    private void generateEntityFiles(Path modulePath, String packageName, String moduleName,
                                   String entityName, String entityComment, String copyright, String author) throws IOException {
        String entityNameCamel = toPascalCase(entityName);
        String entityNameLower = entityName.toLowerCase();

        try {
            // 使用新的构造函数，正确分离模块名和实体名
            DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(modulePath, packageName, moduleName, entityNameCamel, entityNameLower, entityComment, copyright, author);

            // 为实体生成DDD分层文件
            log.debug("    - 生成领域层文件");
            templateGenerator.generateDomainFiles();

            log.debug("    - 生成应用层文件");
            templateGenerator.generateApplicationFiles();

            log.debug("    - 生成基础设施层文件");
            templateGenerator.generateInfrastructureFiles();

            log.debug("    - 生成表现层文件");
            templateGenerator.generatePresentationFiles();
        } catch (IOException e) {
            throw new IOException("生成实体 " + entityName + " 的文件时发生错误", e);
        }
    }

    /**
     * 生成模块级别的文件（包信息、README等）
     */
    private void generateModuleLevelFiles(Path modulePath, String packageName, String moduleName,
                                        String moduleComment, String copyright, String author) throws IOException {
        String entityNameCamel = toPascalCase(moduleName);
        String moduleNameLower = moduleName.toLowerCase();

        try {
            // 注意：这里传入 packageName 作为基础包名，DDDTemplateGenerator 会自动构建完整包名
            DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(modulePath, packageName, entityNameCamel, moduleNameLower, moduleComment, copyright, author);

            // 生成包信息文件
            templateGenerator.generatePackageInfoFiles();

            // 生成 README.md 文件
            templateGenerator.generateReadmeFiles();
        } catch (IOException e) {
            throw new IOException("生成模块 " + moduleName + " 的配置文件时发生错误", e);
        }
    }
}
