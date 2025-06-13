package cn.treedeep.king.tools;

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
import java.util.Map;

/**
 * Copyright © 深圳市树深计算机系统有限公司 版权所有
 * <p>
 * 模板文件生成
 * </p>
 *
 * @author 周广明
 * @since 2025/6/10 06:38
 */
@Slf4j
@RequiredArgsConstructor
public class DDDTemplateGenerator {

    private final Path modulePath;
    private final String entityNameCamel;
    private final Configuration fmConfig;
    private final Map<String, Object> params = new HashMap<>();

    public DDDTemplateGenerator(Path modulePath,
                                String packageName,
                                String entityNameCamel,
                                String moduleName,
                                String moduleComment,
                                String copyright,
                                String author) {

        this.modulePath = modulePath;
        this.entityNameCamel = entityNameCamel;

        params.put("packageName", packageName);
        params.put("entityNameCamel", entityNameCamel);
        params.put("moduleNameLower", moduleName.toLowerCase());
        params.put("moduleComment", moduleComment);
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
     * 专门用于实体生成的构造函数
     * 正确分离模块名（用于包名）和实体名（用于类名和变量名）
     */
    public DDDTemplateGenerator(Path modulePath,
                                String packageName,
                                String moduleName,
                                String entityNameCamel,
                                String entityNameLower,
                                String entityComment,
                                String copyright,
                                String author) {

        this.modulePath = modulePath;
        this.entityNameCamel = entityNameCamel;

        params.put("packageName", packageName);
        params.put("entityNameCamel", entityNameCamel);
        params.put("moduleNameLower", moduleName.toLowerCase());
        params.put("entityNameLower", entityNameLower);
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

    public void generateDomainFiles() throws IOException {
        generateAggregateId();
        generateAggregateRoot();
        generateDomainEvent();
        generateDomainService();
        generateAggregateRepository();
        generateDescription();
        generateItem();
    }

    public void generateApplicationFiles() throws IOException {
        generateCommand();
        generateCommandHandler();
        generateQuery();
        generateQueryResult();
        generateQueryHandler();
        generateDto();
        generateDtoConverter();
        generateItemDto();
        generateApplicationService();
    }

    public void generateInfrastructureFiles() throws IOException {
        generateJpaRepository();
        generateModuleConfig();
        generateAggregateJpaRepository();
        generateAggregateRepositoryImpl();
    }

    public void generatePresentationFiles() throws IOException {
        generateController();
        generateRequestDto();
        generateResponseDto();
    }

    public void generatePackageInfoFiles() throws IOException {
        // generatePackageInfo("domain", "领域层","");
    }

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

    private void writeFile(Path filePath, String content) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
        log.debug("Generated file: {}", filePath);
    }

    private void generateAggregateId() throws IOException {
        String content = processTemplate("domain/EntityId.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + entityNameCamel + "Id.java"), content);
    }

    private void generateAggregateRoot() throws IOException {
        String content = processTemplate("domain/Entity.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + entityNameCamel + ".java"), content);
    }


    private void generateDomainEvent() throws IOException {
        String content = processTemplate("domain/event/EntityCreatedEvent.java.ftl", params);
        writeFile(modulePath.resolve("domain/event/" + entityNameCamel + "CreatedEvent.java"), content);
    }

    private void generateDomainService() throws IOException {
        String content = processTemplate("domain/service/DomainService.java.ftl", params);
        writeFile(modulePath.resolve("domain/service/" + entityNameCamel + "DomainService.java"), content);
    }

    private void generateCommand() throws IOException {
        String content = processTemplate("application/command/CreateCommand.java.ftl", params);
        writeFile(modulePath.resolve("application/command/Create" + entityNameCamel + "Command.java"), content);
    }

    private void generateCommandHandler() throws IOException {
        String content = processTemplate("application/command/CreateCommandHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/command/Create" + entityNameCamel + "CommandHandler.java"), content);
    }

    private void generateQuery() throws IOException {
        String content = processTemplate("application/query/ListQuery.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + entityNameCamel + "ListQuery.java"), content);
    }

    private void generateQueryResult() throws IOException {
        String content = processTemplate("application/query/ListQueryResult.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + entityNameCamel + "ListQueryResult.java"), content);
    }

    private void generateQueryHandler() throws IOException {
        String content = processTemplate("application/query/ListQueryHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + entityNameCamel + "ListQueryHandler.java"), content);
    }

    private void generateDto() throws IOException {
        String content = processTemplate("application/dto/Dto.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + entityNameCamel + "Dto.java"), content);
    }

    private void generateDtoConverter() throws IOException {
        String content = processTemplate("application/dto/DtoConverter.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + entityNameCamel + "DtoConverter.java"), content);
    }

    private void generateJpaRepository() throws IOException {
        String content = processTemplate("infrastructure/repository/JpaRepository.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + entityNameCamel + "JpaRepository.java"), content);
    }

    private void generateModuleConfig() throws IOException {
        String content = processTemplate("infrastructure/ModuleConfig.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/ModuleConfig.java"), content);
    }

    private void generateController() throws IOException {
        String content = processTemplate("presentation/Controller.java.ftl", params);
        writeFile(modulePath.resolve("presentation/" + entityNameCamel + "Controller.java"), content);
    }

    private void generateRequestDto() throws IOException {
        String content = processTemplate("presentation/dto/CreateRequest.java.ftl", params);
        writeFile(modulePath.resolve("presentation/dto/Create" + entityNameCamel + "Request.java"), content);
    }

    private void generateResponseDto() throws IOException {
        String content = processTemplate("presentation/dto/ListResponse.java.ftl", params);
        writeFile(modulePath.resolve("presentation/dto/" + entityNameCamel + "ListResponse.java"), content);
    }

    private void generatePackageInfo(String layer, String layerName, String description) throws IOException {
        params.put("layerName", layerName);
        params.put("description", description);
        params.put("layer", layer);

        String content = processTemplate(layer + "/package-info.java.ftl", params);
        writeFile(modulePath.resolve(layer + "/package-info.java"), content);
    }

    private void generateAggregateRepository() throws IOException {
        String content = processTemplate("domain/EntityRepository.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + entityNameCamel + "AggregateRepository.java"), content);
    }

    private void generateDescription() throws IOException {
        String content = processTemplate("domain/Description.java.ftl", params);
        writeFile(modulePath.resolve("domain/Description.java"), content);
    }

    private void generateItem() throws IOException {
        String content = processTemplate("domain/Item.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + entityNameCamel + "Item.java"), content);
    }

    private void generateItemDto() throws IOException {
        String content = processTemplate("application/dto/ItemDto.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + entityNameCamel + "ItemDto.java"), content);
    }

    private void generateApplicationService() throws IOException {
        String content = processTemplate("application/service/ApplicationService.java.ftl", params);
        writeFile(modulePath.resolve("application/service/" + entityNameCamel + "ApplicationService.java"), content);
    }

    private void generateAggregateJpaRepository() throws IOException {
        String content = processTemplate("infrastructure/repository/AggregateJpaRepository.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + entityNameCamel + "AggregateJpaRepository.java"), content);
    }

    private void generateAggregateRepositoryImpl() throws IOException {
        String content = processTemplate("infrastructure/repository/AggregateRepositoryImpl.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + entityNameCamel + "AggregateRepositoryImpl.java"), content);
    }

    public void generateReadmeFiles() throws IOException {
        Path readmePath = modulePath.resolve("README.md");

        if (Files.exists(readmePath)) {
            // 如果 README.md 已存在，追加内容
            appendToReadme(readmePath);
        } else {
            // 如果不存在，创建新的 README.md
            String content = processTemplate("README.md.ftl", params);
            writeFile(readmePath, content);
        }
    }

    /**
     * 追加内容到现有的 README.md 文件
     */
    private void appendToReadme(Path readmePath) throws IOException {
        String moduleComment = (String) params.get("moduleComment");
        String dateTime = (String) params.get("dateTime");

        String appendContent = String.format("%n%n## %s%n%n> 添加时间: %s%n", moduleComment, dateTime);

        Files.writeString(readmePath, appendContent,
            java.nio.file.StandardOpenOption.CREATE,
            java.nio.file.StandardOpenOption.APPEND);

        log.info("📝 已追加模块信息到 README.md");
    }
}
