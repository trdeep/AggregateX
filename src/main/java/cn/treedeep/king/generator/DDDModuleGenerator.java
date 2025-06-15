package cn.treedeep.king.generator;

import cn.treedeep.king.generator.model.*;
import cn.treedeep.king.generator.model.Module;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DDD模块生成器
 * <p>
 * 重构版本，专注于模块目录结构、package-info.java 和 README.md 的生成
 *
 * @author AggregateX DDD Framework
 * @since 2.0.0
 */
@Slf4j
public class DDDModuleGenerator {

    private static final String BASE_PACKAGE_PATH = "cn/treedeep/king";
    private static final String DEFAULT_AUTHOR = "Rubin";
    private static final String DEFAULT_COPYRIGHT = "深圳市树深计算机系统有限公司";

    public DDDModuleGenerator() {
        printBanner();
    }

    /**
     * 生成模块
     */
    public void generateModules(String projectPath, String packageName, List<Module> modules, boolean isCover) {
        generateModules(projectPath, packageName, modules, isCover, DEFAULT_AUTHOR, DEFAULT_COPYRIGHT);
    }

    /**
     * 生成模块
     */
    public void generateModules(String projectPath, String packageName, List<Module> modules, boolean isCover, String author, String copyright) {
        log.info("🏗️ 开始生成模块...");
        log.info("📁 工程目录：{}", projectPath);
        log.info("📦 将生成 {} 个模块", modules.size());

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            try {
                log.info("📝 正在生成模块 [{}/{}]: {} - {}", i + 1, modules.size(), module.getName(), module.getComment());
                generateModule(projectPath, packageName, module, isCover, author, copyright);
                log.info("✅ 模块 '{}' 生成完成", module.getName());
            } catch (Exception e) {
                log.error("❌ 模块 '{}' 生成失败: {}", module.getName(), e.getMessage(), e);
            }
        }

        log.info("🎉 全部生成完成！");
        printNextSteps();
    }

    /**
     * 生成单个模块
     */
    private void generateModule(String projectPath, String packageName, Module module, boolean isCover, String author, String copyright) throws IOException {

        String moduleName = module.getName();
        String moduleComment = module.getComment();

        // 确定包路径
        String actualPackageName = StringUtils.isBlank(packageName) ?
                BASE_PACKAGE_PATH.replace("/", ".") : packageName;
        String actualPackagePath = actualPackageName.replace(".", "/");

        // 确定模块路径
        Path javaSourcePath = determineJavaSourcePath(projectPath, actualPackagePath);
        Path modulePath = javaSourcePath.resolve(moduleName);

        log.info("📁 创建模块目录结构...");
        createModuleDirectories(modulePath);

        log.info("📄 生成 package-info.java 文件...");
        generatePackageInfoFiles(modulePath, actualPackageName, moduleName, moduleComment, author, copyright);

        log.info("📝 生成 README.md 文件...");
        generateReadmeFile(modulePath, moduleName, moduleComment, module.getAggregateRoots(), author, copyright);

        // 生成聚合根中的值对象和实体
        log.info("🔗 生成值对象和实体文件...");
        generateEntitiesAndValueObjects(modulePath, actualPackageName, moduleName, module.getAggregateRoots(), author, copyright);

        log.info("📍 模块位置: {}", modulePath);
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
     * 创建模块目录结构
     */
    private void createModuleDirectories(Path modulePath) throws IOException {
        // 领域层目录
        Files.createDirectories(modulePath.resolve("domain"));
        Files.createDirectories(modulePath.resolve("domain/event"));
        Files.createDirectories(modulePath.resolve("domain/service"));
        Files.createDirectories(modulePath.resolve("domain/repository"));

        // 应用层目录
        Files.createDirectories(modulePath.resolve("application"));
        Files.createDirectories(modulePath.resolve("application/command"));
        Files.createDirectories(modulePath.resolve("application/command/handler"));
        Files.createDirectories(modulePath.resolve("application/query"));
        Files.createDirectories(modulePath.resolve("application/query/handler"));
        Files.createDirectories(modulePath.resolve("application/query/result"));
        Files.createDirectories(modulePath.resolve("application/dto"));
        Files.createDirectories(modulePath.resolve("application/service"));

        // 基础设施层目录
        Files.createDirectories(modulePath.resolve("infrastructure"));
        Files.createDirectories(modulePath.resolve("infrastructure/repository"));
        Files.createDirectories(modulePath.resolve("infrastructure/service"));

        // 表现层目录
        Files.createDirectories(modulePath.resolve("presentation"));
        Files.createDirectories(modulePath.resolve("presentation/dto"));

        // 接口层目录
        Files.createDirectories(modulePath.resolve("interfaces"));
    }

    /**
     * 生成 package-info.java 文件
     */
    private void generatePackageInfoFiles(Path modulePath, String packageName, String moduleName,
                                          String moduleComment, String author, String copyright) throws IOException {

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                modulePath, packageName, moduleName, toPascalCase(moduleName),
                moduleName.toLowerCase(), moduleComment, copyright, author);

        templateGenerator.generatePackageInfoFiles();
    }

    /**
     * 生成 README.md 文件
     */
    private void generateReadmeFile(Path modulePath, String moduleName, String moduleComment,
                                    List<AggregateRoot> aggregateRoots, String author, String copyright) throws IOException {

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                modulePath, "", moduleName, toPascalCase(moduleName),
                moduleName.toLowerCase(), moduleComment, copyright, author);

        templateGenerator.generateReadmeFiles();
    }

    /**
     * 检测字符串是否已经是驼峰格式
     */
    private boolean isAlreadyCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 检查是否包含下划线、短横线或空格
        if (input.contains("_") || input.contains("-") || input.contains(" ")) {
            return false;
        }

        // 检查首字母是否大写
        if (!Character.isUpperCase(input.charAt(0))) {
            return false;
        }

        // 检查是否包含大写字母（表示驼峰格式）
        boolean hasUpperCase = false;
        for (int i = 1; i < input.length(); i++) {
            if (Character.isUpperCase(input.charAt(i))) {
                hasUpperCase = true;
                break;
            }
        }

        return hasUpperCase;
    }

    /**
     * 转换为PascalCase格式
     */
    private String toPascalCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
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
        System.out.println("DDD Module Generator v2.0.0");
        System.out.println("Generated at: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println();
    }

    /**
     * 生成实体和值对象文件
     */
    private void generateEntitiesAndValueObjects(Path modulePath, String packageName, String moduleName,
                                               List<AggregateRoot> aggregateRoots, String author, String copyright) throws IOException {
        for (AggregateRoot aggregateRoot : aggregateRoots) {
            // 生成聚合根本身
            String aggregateRootName = aggregateRoot.getName();
            String aggregateRootComment = aggregateRoot.getComment();
            String aggregateRootNameCamel = isAlreadyCamelCase(aggregateRootName) ? aggregateRootName : toPascalCase(aggregateRootName);

            DDDTemplateGenerator aggregateGenerator = new DDDTemplateGenerator(
                    modulePath, packageName, moduleName, aggregateRootNameCamel,
                    aggregateRootName, aggregateRootComment, copyright, author);

            // 将聚合根的值对象属性传递给模板（只包含值对象）
            List<Property> aggregateProperties =
                aggregateRoot.getEos().stream()
                    .filter(entity -> entity instanceof ValueObject)  // 只筛选值对象
                    .map(entity -> new Property(entity.getName(), entity.getComment()))
                    .toList();
            aggregateGenerator.addProperties(aggregateProperties);

            // 生成聚合根相关文件
            aggregateGenerator.generateAggregateRoot();
            aggregateGenerator.generateAggregateRootId();
            aggregateGenerator.generateAggregateRepository();
            aggregateGenerator.generateAggregateRepositoryImpl();
            aggregateGenerator.generateAggregateJpaRepository();
            aggregateGenerator.generateDomainServiceInterface();
            aggregateGenerator.generateDomainServiceImpl();

            // 生成应用层代码
            aggregateGenerator.generateCommand();
            aggregateGenerator.generateCommandHandler();
            aggregateGenerator.generateQuery();
            aggregateGenerator.generateQueryHandler();
            aggregateGenerator.generateQueryResult();
            aggregateGenerator.generateDto();
            aggregateGenerator.generateApplicationServiceInterface();
            aggregateGenerator.generateApplicationServiceImpl();

            // 生成领域事件
            aggregateGenerator.generateDomainEvent();

            // 生成表现层代码
            aggregateGenerator.generateController();

            log.debug("Generated aggregate root: {}", aggregateRootName);

            // 生成聚合根的实体和值对象
            for (Entity entity : aggregateRoot.getEos()) {
                String entityName = entity.getName();
                String entityComment = entity.getComment();

                // 保持原始驼峰格式，只有在非驼峰格式时才转换
                String entityNameCamel = isAlreadyCamelCase(entityName) ? entityName : toPascalCase(entityName);

                DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                        modulePath, packageName, moduleName, entityNameCamel,
                        entityName, entityComment, copyright, author);

                // 将属性信息传递给模板
                templateGenerator.addProperties(entity.getProperties());

                if (entity instanceof ValueObject) {
                    templateGenerator.generateValueObject();
                    log.debug("Generated value object: {}", entityName);
                } else {
                    templateGenerator.generateEntity();
                    log.debug("Generated entity: {}", entityName);
                }
            }
        }
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
