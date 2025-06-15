package cn.treedeep.king.generator;

import cn.treedeep.king.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public void generateModules(String projectPath, String packageName, List<ModuleInfo> modules, boolean isCover) {
        generateModules(projectPath, packageName, modules, isCover, DEFAULT_AUTHOR, DEFAULT_COPYRIGHT);
    }

    /**
     * 生成模块
     */
    public void generateModules(String projectPath, String packageName, List<ModuleInfo> modules, boolean isCover, String author, String copyright) {
        log.info("🏗️ 开始生成模块...");
        log.info("📁 工程目录：{}", projectPath);
        log.info("📦 将生成 {} 个模块", modules.size());

        for (int i = 0; i < modules.size(); i++) {
            ModuleInfo module = modules.get(i);
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
    private void generateModule(String projectPath, String packageName, ModuleInfo module, boolean isCover, String author, String copyright) throws IOException {

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
        generateReadmeFile(modulePath, moduleName, moduleComment, module.getRemarks(), module.getAggregateRoots(), author, copyright);

        // 生成聚合根中的值对象和实体
        log.info("🔗 生成值对象和实体文件...");
        generateEntitiesAndValueObjects(modulePath, actualPackageName, moduleName, module.getAggregateRoots(), author, copyright);

        // 生成模块级的领域事件
        log.info("📨 生成领域事件文件...");
        generateDomainEvents(modulePath, actualPackageName, moduleName, module.getDomainEvents(), author, copyright);

        // 生成模块级的应用服务
        log.info("🔧 生成应用服务文件...");
        generateApplicationServices(modulePath, actualPackageName, moduleName, module.getApplicationServices(), author, copyright);

        // 生成模块配置
        log.info("⚙️ 生成模块配置文件...");
        generateModuleConfig(modulePath, actualPackageName, moduleName, moduleComment, author, copyright);

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
    private void generateReadmeFile(Path modulePath, String moduleName, String moduleComment, String remarks,
                                    List<AggregateRoot> aggregateRoots, String author, String copyright) throws IOException {

        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                modulePath, "", moduleName, toPascalCase(moduleName),
                moduleName.toLowerCase(), moduleComment, copyright, author);

        // 添加 remarks 参数到模板变量中
        templateGenerator.addParam("moduleRemarks", remarks != null ? remarks : "");

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

            // 按照新设计：聚合根只包含直接属性和AggregateRootProperty嵌套的值对象
            List<Property> aggregateDirectProperties = new ArrayList<>();
            List<Property> aggregateEmbeddedValueObjects = new ArrayList<>();

            // 分离普通属性和AggregateRootProperty
            for (Property property : aggregateRoot.getProperties()) {
                if (Property.AggregateRootProperty.isAggregateRootProperty(property)) {
                    // AggregateRootProperty转换为嵌套值对象
                    // 需要将属性名转换为正确的类型名（首字母大写）
                    String valueObjectTypeName = property.getName();
                    if (valueObjectTypeName.endsWith("ValueObject")) {
                        // 如果属性名以ValueObject结尾，去掉后缀并首字母大写
                        valueObjectTypeName = valueObjectTypeName.substring(0, valueObjectTypeName.length() - "ValueObject".length());
                        valueObjectTypeName = Character.toUpperCase(valueObjectTypeName.charAt(0)) + valueObjectTypeName.substring(1);
                    } else {
                        // 否则直接首字母大写
                        valueObjectTypeName = Character.toUpperCase(valueObjectTypeName.charAt(0)) + valueObjectTypeName.substring(1);
                    }

                    // 创建一个新的Property，名称是valueObjectTypeName，这样模板就知道类型了
                    Property embeddedProperty = new Property(valueObjectTypeName, property.getComment());
                    aggregateEmbeddedValueObjects.add(embeddedProperty);
                } else {
                    // 普通属性
                    aggregateDirectProperties.add(property);
                }
            }

            // 将所有属性传递给模板
            List<Property> allAggregateProperties = new ArrayList<>(aggregateDirectProperties);
            allAggregateProperties.addAll(aggregateEmbeddedValueObjects);

            aggregateGenerator.addProperties(allAggregateProperties);

            // 单独传递直接属性用于普通字段
            aggregateGenerator.addParam("aggregateProperties", aggregateDirectProperties);
            // 单独传递值对象属性用于@Embedded注解
            aggregateGenerator.addParam("valueObjectProperties", aggregateEmbeddedValueObjects);
            // 传递领域方法
            aggregateGenerator.addParam("domainMethods", aggregateRoot.getMethods());

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

            // 生成聚合根的实体和值对象（这些与聚合根同级，在domain层）
            for (Entity entity : aggregateRoot.getEos()) {
                String entityName = entity.getName();
                String entityComment = entity.getComment();

                // 保持原始驼峰格式，只有在非驼峰格式时才转换
                String entityNameCamel = isAlreadyCamelCase(entityName) ? entityName : toPascalCase(entityName);

                DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                        modulePath, packageName, moduleName, entityNameCamel,
                        entityName, entityComment, copyright, author);

                // 分离普通属性和值对象属性
                List<Property> regularProperties = new ArrayList<>();
                List<Property> entityValueObjectProperties = new ArrayList<>();

                for (Property property : entity.getProperties()) {
                    if (Property.ValueObjectProperty.isValueObjectProperty(property)) {
                        entityValueObjectProperties.add(property);
                    } else {
                        regularProperties.add(property);
                    }
                }

                // 将属性信息传递给模板
                templateGenerator.addProperties(entity.getProperties());
                templateGenerator.addParam("regularProperties", regularProperties);
                templateGenerator.addParam("valueObjectProperties", entityValueObjectProperties);

                if (entity instanceof ValueObject) {
                    templateGenerator.generateValueObject();
                    log.debug("Generated value object: {}", entityName);
                } else {
                    templateGenerator.generateEntity();
                    log.debug("Generated entity: {}", entityName);
                }
            }

            // 生成AggregateRootProperty引用的值对象（确保这些被嵌入的值对象在domain层生成）
            for (Property property : aggregateRoot.getProperties()) {
                if (Property.AggregateRootProperty.isAggregateRootProperty(property)) {
                    // 从属性名推导值对象名称和类型
                    String valueObjectTypeName = property.getName();
                    if (valueObjectTypeName.endsWith("ValueObject")) {
                        valueObjectTypeName = valueObjectTypeName.substring(0, valueObjectTypeName.length() - "ValueObject".length());
                        valueObjectTypeName = Character.toUpperCase(valueObjectTypeName.charAt(0)) + valueObjectTypeName.substring(1);
                    } else {
                        valueObjectTypeName = Character.toUpperCase(valueObjectTypeName.charAt(0)) + valueObjectTypeName.substring(1);
                    }

                    // 检查这个值对象是否已经在aggregateRoot.getEos()中存在
                    boolean alreadyExists = false;
                    for (Entity entity : aggregateRoot.getEos()) {
                        if (entity.getName().equals(valueObjectTypeName)) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    // 如果不存在，则创建并生成这个值对象
                    if (!alreadyExists) {
                        String valueObjectComment = property.getComment() != null ? property.getComment() : valueObjectTypeName + "值对象";

                        DDDTemplateGenerator valueObjectGenerator = new DDDTemplateGenerator(
                                modulePath, packageName, moduleName, valueObjectTypeName,
                                valueObjectTypeName, valueObjectComment, copyright, author);

                        // 为简单的值对象创建一个基本的value属性
                        List<Property> basicProperties = new ArrayList<>();
                        basicProperties.add(new Property("value", "值"));

                        valueObjectGenerator.addProperties(basicProperties);
                        valueObjectGenerator.addParam("regularProperties", basicProperties);
                        valueObjectGenerator.addParam("valueObjectProperties", new ArrayList<>());

                        valueObjectGenerator.generateValueObject();
                        log.debug("Generated embedded value object: {}", valueObjectTypeName);
                    }
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

    /**
     * 生成模块级领域事件
     */
    private void generateDomainEvents(Path modulePath, String packageName, String moduleName,
                                     List<DomainEvent> domainEvents, String author, String copyright) throws IOException {
        for (DomainEvent domainEvent : domainEvents) {
            String eventName = domainEvent.getName();
            String eventComment = domainEvent.getComment();
            String aggregateRootName = domainEvent.getAggregateRootName();

            // 保持原始驼峰格式，只有在非驼峰格式时才转换
            String eventNameCamel = isAlreadyCamelCase(eventName) ? eventName : toPascalCase(eventName);

            DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                    modulePath, packageName, moduleName, eventNameCamel,
                    eventName, eventComment, copyright, author);

            // 添加事件相关的参数
            templateGenerator.addParam("eventName", eventName);
            templateGenerator.addParam("eventNameCamel", eventNameCamel);
            templateGenerator.addParam("eventComment", eventComment);
            templateGenerator.addParam("aggregateRootName", aggregateRootName);
            templateGenerator.addParam("tableName", domainEvent.getTableName());
            templateGenerator.addParam("eventFields", domainEvent.getFields());

            // 添加聚合根类名参数（如果有关联的聚合根）
            if (aggregateRootName != null && !aggregateRootName.isEmpty()) {
                templateGenerator.addParam("aggregateRootClass", aggregateRootName);
            }

            // 生成领域事件文件
            String content = templateGenerator.processTemplate("domain/event/DomainEventTemplate.java.ftl", templateGenerator.getParams());
            templateGenerator.writeFile(modulePath.resolve("domain/event/" + eventNameCamel + ".java"), content);

            log.debug("Generated domain event: {}", eventNameCamel);
        }
    }

    /**
     * 生成模块级应用服务
     */
    private void generateApplicationServices(Path modulePath, String packageName, String moduleName,
                                           List<ApplicationService> applicationServices, String author, String copyright) throws IOException {
        for (ApplicationService applicationService : applicationServices) {
            String serviceName = applicationService.getName();
            String serviceComment = applicationService.getComment();

            // 保持原始驼峰格式，只有在非驼峰格式时才转换
            String serviceNameCamel = isAlreadyCamelCase(serviceName) ? serviceName : toPascalCase(serviceName);

            DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                    modulePath, packageName, moduleName, serviceNameCamel,
                    serviceName, serviceComment, copyright, author);

            // 添加应用服务相关的参数
            templateGenerator.addParam("serviceName", serviceName);
            templateGenerator.addParam("serviceNameCamel", serviceNameCamel);
            templateGenerator.addParam("serviceComment", serviceComment);
            templateGenerator.addParam("interfaceName", applicationService.getInterfaceName());
            templateGenerator.addParam("implementationName", applicationService.getImplementationName());
            templateGenerator.addParam("serviceMethods", applicationService.getMethods());

            // 生成应用服务接口
            String interfaceContent = templateGenerator.processTemplate("application/service/ApplicationServiceInterfaceTemplate.java.ftl", templateGenerator.getParams());
            templateGenerator.writeFile(modulePath.resolve("application/service/" + applicationService.getInterfaceName() + ".java"), interfaceContent);

            // 生成应用服务实现
            String implContent = templateGenerator.processTemplate("application/service/impl/ApplicationServiceImplTemplate.java.ftl", templateGenerator.getParams());
            templateGenerator.writeFile(modulePath.resolve("application/service/impl/" + applicationService.getImplementationName() + ".java"), implContent);

            log.debug("Generated application service: {}", serviceNameCamel);
        }
    }

    /**
     * 生成模块配置
     */
    private void generateModuleConfig(Path modulePath, String packageName, String moduleName,
                                    String moduleComment, String author, String copyright) throws IOException {
        DDDTemplateGenerator templateGenerator = new DDDTemplateGenerator(
                modulePath, packageName, moduleName, toPascalCase(moduleName),
                moduleName.toLowerCase(), moduleComment, copyright, author);

        templateGenerator.generateModuleConfig();
        log.debug("Generated module config for module: {}", moduleName);
    }
}
