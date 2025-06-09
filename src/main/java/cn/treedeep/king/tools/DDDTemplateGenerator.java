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
import java.time.LocalDateTime;
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
    private final String moduleNameCamel;
    private final Configuration fmConfig;
    private final Map<String, Object> params = new HashMap<>();


    public DDDTemplateGenerator(Path modulePath, String moduleNameCamel, String moduleNameLower, String moduleComment) throws IOException {
        this.modulePath = modulePath;
        this.moduleNameCamel = moduleNameCamel;

        params.put("moduleNameCamel", moduleNameCamel);
        params.put("moduleNameLower", moduleNameLower);
        params.put("moduleComment", moduleComment);
        params.put("copyright", "深圳市树深计算机系统有限公司");
        params.put("author", "AggregateX");
        params.put("dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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
        generateRepository();
        generateDomainEvent();
        generateDomainService();
    }

    public void generateApplicationFiles() throws IOException {
        generateCommand();
        generateCommandHandler();
        generateQuery();
        generateQueryResult();
        generateQueryHandler();
        generateDto();
        generateDtoConverter();
    }

    public void generateInfrastructureFiles() throws IOException {
        generateJpaRepository();
        generateRepositoryImpl();
        generateModuleConfig();
    }

    public void generatePresentationFiles() throws IOException {
        generateController();
        generateRequestDto();
        generateResponseDto();
    }

    public void generatePackageInfoFiles() throws IOException {
        generatePackageInfo("domain", "领域层",
                "包含核心业务逻辑和领域模型：\n" +
                        " * - 聚合根: " + moduleNameCamel + "\n" +
                        " * - 值对象: " + moduleNameCamel + "Id\n" +
                        " * - 领域事件: " + moduleNameCamel + "CreatedEvent\n" +
                        " * - 领域服务: " + moduleNameCamel + "DomainService\n" +
                        " * - 仓储接口: " + moduleNameCamel + "Repository");

        generatePackageInfo("application", "应用层",
                "协调领域层和基础设施层：\n" +
                        " * - 命令处理: Create" + moduleNameCamel + "Command\n" +
                        " * - 查询处理: " + moduleNameCamel + "ListQuery\n" +
                        " * - 数据传输对象: " + moduleNameCamel + "Dto\n" +
                        " * - 应用服务: " + moduleNameCamel + "ApplicationService");

        generatePackageInfo("infrastructure", "基础设施层",
                "提供技术实现和外部依赖：\n" +
                        " * - 仓储实现: " + moduleNameCamel + "RepositoryImpl\n" +
                        " * - JPA 仓储: " + moduleNameCamel + "JpaRepository\n" +
                        " * - 外部服务客户端\n" +
                        " * - 消息发布器实现");

        generatePackageInfo("presentation", "表现层",
                "处理用户界面和HTTP请求：\n" +
                        " * - REST 控制器: " + moduleNameCamel + "Controller\n" +
                        " * - 请求DTO: Create" + moduleNameCamel + "Request\n" +
                        " * - 响应DTO: " + moduleNameCamel + "ListResponse\n" +
                        " * - 数据验证和转换");

        generatePackageInfo("interfaces", "接口层",
                """
                        负责系统间通信和外部系统集成：
                         * - RPC 服务接口
                         * - 消息队列处理
                         * - 外部API客户端
                         * - 系统间数据传输对象""");
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
        String content = processTemplate("domain/Id.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + moduleNameCamel + "Id.java"), content);
    }

    private void generateAggregateRoot() throws IOException {
        String content = processTemplate("domain/Main.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + moduleNameCamel + ".java"), content);
    }

    private void generateRepository() throws IOException {
        String content = processTemplate("domain/Repository.java.ftl", params);
        writeFile(modulePath.resolve("domain/" + moduleNameCamel + "Repository.java"), content);
    }

    private void generateDomainEvent() throws IOException {
        String content = processTemplate("domain/event/CreatedEvent.java.ftl", params);
        writeFile(modulePath.resolve("domain/event/" + moduleNameCamel + "CreatedEvent.java"), content);
    }

    private void generateDomainService() throws IOException {
        String content = processTemplate("domain/service/DomainService.java.ftl", params);
        writeFile(modulePath.resolve("domain/service/" + moduleNameCamel + "DomainService.java"), content);
    }

    private void generateCommand() throws IOException {
        String content = processTemplate("application/command/CreateCommand.java.ftl", params);
        writeFile(modulePath.resolve("application/command/Create" + moduleNameCamel + "Command.java"), content);
    }

    private void generateCommandHandler() throws IOException {
        String content = processTemplate("application/command/CreateCommandHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/command/Create" + moduleNameCamel + "CommandHandler.java"), content);
    }

    private void generateQuery() throws IOException {
        String content = processTemplate("application/query/ListQuery.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + moduleNameCamel + "ListQuery.java"), content);
    }

    private void generateQueryResult() throws IOException {
        String content = processTemplate("application/query/ListQueryResult.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + moduleNameCamel + "ListQueryResult.java"), content);
    }

    private void generateQueryHandler() throws IOException {
        String content = processTemplate("application/query/ListQueryHandler.java.ftl", params);
        writeFile(modulePath.resolve("application/query/" + moduleNameCamel + "ListQueryHandler.java"), content);
    }

    private void generateDto() throws IOException {
        String content = processTemplate("application/dto/Dto.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + moduleNameCamel + "Dto.java"), content);
    }

    private void generateDtoConverter() throws IOException {
        String content = processTemplate("application/dto/DtoConverter.java.ftl", params);
        writeFile(modulePath.resolve("application/dto/" + moduleNameCamel + "DtoConverter.java"), content);
    }

    private void generateJpaRepository() throws IOException {
        String content = processTemplate("infrastructure/repository/JpaRepository.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + moduleNameCamel + "JpaRepository.java"), content);
    }

    private void generateRepositoryImpl() throws IOException {
        String content = processTemplate("infrastructure/repository/RepositoryImpl.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/repository/" + moduleNameCamel + "RepositoryImpl.java"), content);
    }

    private void generateModuleConfig() throws IOException {
        String content = processTemplate("infrastructure/ModuleConfig.java.ftl", params);
        writeFile(modulePath.resolve("infrastructure/ModuleConfig.java"), content);
    }

    private void generateController() throws IOException {
        String content = processTemplate("presentation/Controller.java.ftl", params);
        writeFile(modulePath.resolve("presentation/" + moduleNameCamel + "Controller.java"), content);
    }

    private void generateRequestDto() throws IOException {
        String content = processTemplate("presentation/dto/CreateRequest.java.ftl", params);
        writeFile(modulePath.resolve("presentation/dto/Create" + moduleNameCamel + "Request.java"), content);
    }

    private void generateResponseDto() throws IOException {
        String content = processTemplate("presentation/dto/ListResponse.java.ftl", params);
        writeFile(modulePath.resolve("presentation/dto/" + moduleNameCamel + "ListResponse.java"), content);
    }

    private void generatePackageInfo(String layer, String layerName, String description) throws IOException {
        params.put("layerName", layerName);
        params.put("description", description);
        params.put("layer", layer);

        String content = processTemplate(layer + "/package-info.java.ftl", params);
        writeFile(modulePath.resolve(layer + "/package-info.java"), content);
    }
}
