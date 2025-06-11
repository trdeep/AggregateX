package cn.treedeep.king.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    public static void main(String[] args) {
        new DDDModuleGenerator().runInteractiveMode();
    }

    /**
     * 交互模式运行
     */
    private void runInteractiveMode() {
        printBanner();
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("🎯 AggregateX DDD模块生成器");
            System.out.println("═══════════════════════════");
            System.out.println();

            System.out.print("🧑‍💻 请输入作者信息: ");
            String author = scanner.nextLine().trim();

            System.out.print("©️ 请输入版权信息: ");
            String copyright = scanner.nextLine().trim();
            System.out.println();

            boolean continueGenerating = true;
            while (continueGenerating) {

                // 获取项目路径
                System.out.print("📁 请输入项目路径 (默认为当前路径 '.'): ");
                String projectPath = scanner.nextLine().trim();
                if (projectPath.isEmpty()) {
                    projectPath = ".";
                }

                // 获取模块名称
                System.out.print("📦 请输入模块信息，可空格带注释 (如: order 订单}): ");
                String moduleInfo = scanner.nextLine().trim();

                if (moduleInfo.isEmpty()) {
                    log.error("❌ 模块信息不能为空");
                    continue;
                }

                System.out.println();
                System.out.println("🚀 开始生成模块...");

                try {

                    if (moduleInfo.contains(" ")) {
                        String[] split = moduleInfo.split(" ");
                        generateModule(projectPath, split[0], split[1], copyright, author);
                    } else {
                        generateModule(projectPath, moduleInfo, null, copyright, author);
                    }

                    System.out.println();
                    System.out.println("🎉 模块生成成功!");
                    System.out.println("📍 请查看生成的文件并根据业务需求进行调整");
                } catch (Exception e) {
                    log.error("❌ 模块生成失败", e);
                }

                // Ask if the user wants to continue generating modules
                System.out.print("\n是否继续生成模块？ (y/N): ");
                String response = null;
                try {
                    response = scanner.nextLine().trim().toLowerCase();
                } catch (Exception ignored) {
                }
                if (!"y".equals(response) && !"yes".equals(response)) {
                    continueGenerating = false;
                }
            }
        }
    }

    /**
     * 生成DDD模块
     */
    public void generateModule(String projectPath, String moduleName, String moduleComment, String copyright, String author) throws IOException {
        log.info("🏗️ 工程目录：{}", projectPath);

        if (StringUtils.isBlank(copyright)) {
            copyright = "深圳市树深计算机系统有限公司";
        }
        if (StringUtils.isBlank(author)) {
            author = "AggregateX";
        }

        validateInputs(projectPath, moduleName);

        Path javaSourcePath = determineJavaSourcePath(projectPath);
        Path modulePath = javaSourcePath.resolve(moduleName);

        // 检查模块是否已存在
        if (Files.exists(modulePath)) {
            log.warn("⚠️ 模块 '{}' 已存在于路径: {}", moduleName, modulePath);
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("是否要覆盖现有模块? (y/N): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!"y".equals(response) && !"yes".equals(response)) {
                    log.info("操作已取消");
                    return;
                }
            }
            deleteDirectory(modulePath);
        }

        log.info("📁 创建目录结构...");
        createDirectoryStructure(modulePath);

        log.info("📝 生成模板文件...");
        generateTemplateFiles(modulePath, moduleName, moduleComment, copyright, author);

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
    private Path determineJavaSourcePath(String projectPath) {
        Path projectDir = Paths.get(projectPath);

        // 优先检查标准路径
        Path standardPath = projectDir.resolve("src/main/java").resolve(BASE_PACKAGE_PATH);
        if (Files.exists(standardPath)) {
            return standardPath;
        }

        // 检查example路径
        Path examplePath = projectDir.resolve("example/src/main/java").resolve(BASE_PACKAGE_PATH);
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
        String moduleName = modulePath.getFileName().toString();

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
    private void generateTemplateFiles(Path modulePath, String moduleName, String moduleComment, String copyright, String author) throws IOException {
        String moduleNameCamel = toPascalCase(moduleName);
        String moduleNameLower = moduleName.toLowerCase();

        if (moduleComment.isEmpty()) {
            moduleComment = moduleNameCamel;
        }

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(modulePath, moduleNameCamel, moduleNameLower, moduleComment, copyright, author);

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
}
