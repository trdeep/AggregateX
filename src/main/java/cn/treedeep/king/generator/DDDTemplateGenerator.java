package cn.treedeep.king.generator;

import cn.treedeep.king.generator.model.Property;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 深圳市树深计算机系统有限公司 版权所有
 * <p>
 * 模板文件生成器
 * <p>
 * 重构版本，专注于 package-info.java 和 README.md 的生成
 * </p>
 *
 * @author 周广明
 * @since 2.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class DDDTemplateGenerator {

    private final Path modulePath;
    private final Configuration fmConfig;
    private final Map<String, Object> params = new HashMap<>();

    /**
     * DDDTemplateGenerator 构造函数
     *
     * @param modulePath         模块路径
     * @param packageName        基础包名 (如: cn.treedeep.king)
     * @param moduleName         模块名称 (如: identity)
     * @param entityNameCamel    实体名称驼峰形式 (如: LoginRecord)
     * @param entityNameOriginal 实体名称原始形式 (如: LoginRecord)
     * @param entityComment      实体注释
     * @param copyright          版权信息
     * @param author             作者
     */
    public DDDTemplateGenerator(Path modulePath,
                                String packageName,
                                String moduleName,
                                String entityNameCamel,
                                String entityNameOriginal,
                                String entityComment,
                                String copyright,
                                String author) {

        this.modulePath = modulePath;

        params.put("packageName", packageName);
        params.put("entityNameCamel", entityNameCamel);
        params.put("moduleNameLower", moduleName.toLowerCase());
        params.put("entityNameLower", entityNameOriginal.toLowerCase());
        params.put("entityTableName", toSnakeCase(entityNameCamel));
        params.put("entityComment", entityComment);
        params.put("moduleComment", entityComment);
        params.put("copyright", copyright);
        params.put("author", author);
        params.put("dateTime", OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Initialize FreeMarker configuration
        this.fmConfig = new Configuration(Configuration.VERSION_2_3_31);
        this.fmConfig.setDefaultEncoding("UTF-8");
        this.fmConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.fmConfig.setLogTemplateExceptions(false);
        this.fmConfig.setWrapUncheckedExceptions(true);
        this.fmConfig.setFallbackOnNullLoopVariable(false);

        // Set the template loader to load from classpath resources
        this.fmConfig.setClassForTemplateLoading(getClass(), "/");
        this.fmConfig.setTemplateLoader(new freemarker.cache.ClassTemplateLoader(getClass(), "/template"));
    }

    /**
     * 生成包信息文件
     */
    public void generatePackageInfoFiles() throws IOException {
        generatePackageInfo("application");
        generatePackageInfo("application/service");
        generatePackageInfo("domain");
        generatePackageInfo("domain/service");
        generatePackageInfo("infrastructure");
        generatePackageInfo("infrastructure/service");
        generatePackageInfo("interfaces");
        generatePackageInfo("presentation");
    }

    /**
     * 生成 README.md 文件
     */
    public void generateReadmeFiles() throws IOException {
        Path readmePath = modulePath.resolve("README.md");
        if (!Files.exists(readmePath)) {
            String content = processTemplate("README.md.ftl", params);
            writeFile(readmePath, content);
        }
    }

    /**
     * 生成值对象文件
     */
    public void generateValueObject() throws IOException {
        String content = processTemplate("domain/ValueObject.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + params.get("entityNameCamel") + ".java"), content);
    }

    /**
     * 生成实体文件
     */
    public void generateEntity() throws IOException {
        String content = processTemplate("domain/Entity.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + params.get("entityNameCamel") + ".java"), content);
    }

    /**
     * 生成聚合根
     */
    public void generateAggregateRoot() throws IOException {
        String content = processTemplate("domain/AggregateRoot.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + params.get("entityNameCamel") + ".java"), content);
    }

    /**
     * 生成聚合根ID
     */
    public void generateAggregateRootId() throws IOException {
        String content = processTemplate("domain/AggregateRootId.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + params.get("entityNameCamel") + "Id.java"), content);
    }

    /**
     * 生成聚合仓储接口
     */
    public void generateAggregateRepository() throws IOException {
        String content = processTemplate("domain/repository/AggregateRepository.java.ftl", params);
        writeFile(modulePath.resolve("domain/repository/" + params.get("entityNameCamel") + "AggregateRepository.java"), content);
    }

    /**
     * 生成聚合仓储实现
     */
    public void generateAggregateRepositoryImpl() throws IOException {
        String content = processTemplate("infrastructure/repository/AggregateRepositoryImpl.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + params.get("entityNameCamel") + "AggregateRepositoryImpl.java"), content);
    }

    /**
     * 生成聚合JPA仓储
     */
    public void generateAggregateJpaRepository() throws IOException {
        String content = processTemplate("infrastructure/repository/AggregateJpaRepository.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + params.get("entityNameCamel") + "AggregateJpaRepository.java"), content);
    }

    /**
     * 生成领域服务接口
     */
    public void generateDomainServiceInterface() throws IOException {
        String content = processTemplate("domain/service/DomainServiceInterface.java.ftl", params);
        writeFile(modulePath.resolve("domain/service/DomainService.java"), content);
    }

    /**
     * 生成领域服务实现
     */
    public void generateDomainServiceImpl() throws IOException {
        String content = processTemplate("domain/service/impl/DomainServiceImpl.java.ftl", params);
        writeFile(modulePath.resolve("domain/service/impl/DomainServiceImpl.java"), content);
    }

    /**
     * 生成命令
     */
    public void generateCommand() throws IOException {
        String content = processTemplate("application/command/Command.java.ftl", params);
        writeFile(modulePath.resolve("application/command/SayHelloCommand.java"), content);
    }

    /**
     * 生成命令处理器
     */
    public void generateCommandHandler() throws IOException {
        String content = processTemplate("application/command/handler/CommandHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/command/handler/SayHelloCommandHandler.java"), content);
    }

    /**
     * 生成查询
     */
    public void generateQuery() throws IOException {
        String content = processTemplate("application/query/Query.java.ftl", params);
        writeFile(modulePath.resolve("application/query/SayHelloQuery.java"), content);
    }

    /**
     * 生成查询处理器
     */
    public void generateQueryHandler() throws IOException {
        String content = processTemplate("application/query/handler/QueryHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/query/handler/SayHelloQueryHandler.java"), content);
    }

    /**
     * 生成查询结果
     */
    public void generateQueryResult() throws IOException {
        String content = processTemplate("application/query/result/QueryResult.java.ftl", params);
        writeFile(modulePath.resolve("application/query/result/ListQueryResult.java"), content);
    }

    /**
     * 生成DTO
     */
    public void generateDto() throws IOException {
        String content = processTemplate("application/dto/Dto.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + params.get("entityNameCamel") + "Dto.java"), content);
    }

    /**
     * 生成应用服务接口
     */
    public void generateApplicationServiceInterface() throws IOException {
        String content = processTemplate("application/service/ApplicationServiceInterface.java.ftl", params);
        writeFile(modulePath.resolve("application/service/ApplicationService.java"), content);
    }

    /**
     * 生成应用服务实现
     */
    public void generateApplicationServiceImpl() throws IOException {
        String content = processTemplate("application/service/impl/ApplicationServiceImpl.java.ftl", params);
        writeFile(modulePath.resolve("application/service/impl/ApplicationServiceImpl.java"), content);
    }

    /**
     * 生成领域事件
     */
    public void generateDomainEvent() throws IOException {
        String content = processTemplate("domain/event/DomainEvent.java.ftl", params);
        writeFile(modulePath.resolve("domain/event/SayHelloEvent.java"), content);
    }

    /**
     * 生成控制器
     */
    public void generateController() throws IOException {
        String content = processTemplate("presentation/Controller.java.ftl", params);
        writeFile(modulePath.resolve("presentation/SayHelloController.java"), content);
    }

    /**
     * 添加属性信息到模板参数
     */
    public void addProperties(List<Property> properties) {
        params.put("properties", properties);
    }

    /**
     * 添加额外的模板参数
     */
    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    /**
     * 处理模板
     */
    private String processTemplate(String templateName, Map<String, Object> params) throws IOException {
        try {
            Template template = fmConfig.getTemplate(templateName);
            Writer out = new StringWriter();
            template.process(params, out);
            return out.toString();
        } catch (TemplateException e) {
            throw new IOException("Error processing template: " + templateName, e);
        }
    }

    /**
     * 写入文件
     */
    private void writeFile(Path filePath, String content) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
        log.debug("Generated file: {}", filePath);
    }

    /**
     * 生成包信息文件
     */
    private void generatePackageInfo(String layer) throws IOException {
        String content = processTemplate(layer + "/package-info.java.ftl", params);
        writeFile(modulePath.resolve(layer + "/package-info.java"), content);
    }

    /**
     * 将驼峰格式转换为蛇形格式
     */
    private String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
        }
        return result.toString();
    }
}
