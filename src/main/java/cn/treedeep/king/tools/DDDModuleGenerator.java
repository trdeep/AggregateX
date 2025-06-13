package cn.treedeep.king.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.util.function.Tuple4;

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
        String moduleNameCamel = toPascalCase(moduleName);
        String moduleNameLower = moduleName.toLowerCase();

        if (moduleComment.isEmpty()) {
            moduleComment = moduleNameCamel;
        }

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(modulePath, packageName, moduleNameCamel, moduleNameLower, moduleComment, copyright, author);

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
     *
     * @param projectPath 项目路径
     * @param packageName 包名
     * @param isCover     是否覆盖
     * @param modules     模块信息列表，每个元素为【元组[模块名, 模块注释, 实体类列表, 实体类描述列表]】
     * @param author      作者
     * @param copyright   版权信息
     */
    public void generateModules(String projectPath, String packageName, boolean isCover,
                                List<Tuple4<String, String, List<String>, List<String>>> modules,
                                String author,
                                String copyright) {

        log.info("🏗️ 开始批量生成模块...");
        log.info("📁 工程目录：{}", projectPath);
        log.info("📦 将生成 {} 个模块", modules.size());

        for (int i = 0; i < modules.size(); i++) {
            Tuple4<String, String, List<String>, List<String>> moduleInfo = modules.get(i);
            String moduleName = moduleInfo.getT1();
            String moduleComment = moduleInfo.getT2();
            List<String> entities = moduleInfo.getT3();
            List<String> entityDescriptions = moduleInfo.getT4();

            try {
                log.info("📝 正在生成模块 [{}/{}]: {} - {}", i + 1, modules.size(), moduleName, moduleComment);

                // 为每个模块生成基础结构
                generateModuleWithEntities(projectPath, packageName, isCover, moduleName, moduleComment, entities, entityDescriptions, copyright, author);

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
                                            List<String> entities,
                                            List<String> entityDescriptions,
                                            String copyright,
                                            String author) throws IOException {

        // 调用原有的生成方法
        generateModule(projectPath, packageName, isCover, moduleName, moduleComment, copyright, author);

        // 如果提供了额外的实体信息，可以在这里扩展生成逻辑
        if (entities != null && !entities.isEmpty()) {
            log.info("📋 模块 '{}' 包含 {} 个实体: {}", moduleName, entities.size(), entities);
            // 这里可以根据实体列表生成额外的文件或修改现有模板
            // 当前保持与原有逻辑兼容
        }
    }
}
