package cn.treedeep.king.core.infrastructure.validation;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * DDD架构校验器
 * <p>
 * 基于DDD设计原则的架构校验器，确保代码结构符合以下DDD规范：
 * <ul>
 * <li>领域驱动设计分层架构</li>
 * <li>聚合根和实体的设计约束</li>
 * <li>仓储模式的正确实现</li>
 * <li>领域服务与应用服务的职责分离</li>
 * <li>事件驱动架构的规范性</li>
 * <li>表现层设计原则和约束</li>
 * <li>防腐层模式的正确实现</li>
 * <li>包依赖关系和循环依赖检测</li>
 * </ul>
 * <p>
 * 验证规则遵循以下DDD设计原则：
 * <ol>
 * <li><strong>领域层纯净性</strong>：领域层不应依赖于应用层、基础设施层、接口层或表现层</li>
 * <li><strong>依赖倒置</strong>：高层模块不应依赖低层模块，都应依赖抽象</li>
 * <li><strong>聚合边界</strong>：聚合根应控制其内部实体的访问</li>
 * <li><strong>仓储抽象</strong>：仓储接口定义在领域层，实现在基础设施层</li>
 * <li><strong>应用服务协调</strong>：应用服务只负责流程编排，不包含业务逻辑</li>
 * <li><strong>表现层分离</strong>：表现层应使用DTO而非直接使用领域实体</li>
 * </ol>
 *
 * @author 树深 DDD 架构团队
 * @since 1.0.0 2025/6/8 20:16
 */
@Component("king_admin_ddd.ArchitectureValidator")
public final class ArchitectureValidator {

    private static final Logger log = LoggerFactory.getLogger(ArchitectureValidator.class);
    private final ArchitectureValidationProperties properties;

    /**
     * 创建架构校验器实例
     *
     * @param properties 架构校验配置属性
     */
    public ArchitectureValidator(ArchitectureValidationProperties properties) {
        this.properties = properties;
    }

    private String getDomainLayer() {
        return properties.getLayers().getDomain();
    }

    private String getApplicationLayer() {
        return properties.getLayers().getApplication();
    }

    private String getInfrastructureLayer() {
        return properties.getLayers().getInfrastructure();
    }

    private String getInterfacesLayer() {
        return properties.getLayers().getInterfaces();
    }

    private String getPresentationLayer() {
        return properties.getLayers().getPresentation();
    }

    private String getSharedLayer() {
        return properties.getLayers().getShared();
    }

    private String getAntiCorruptionLayer() {
        return properties.getLayers().getAntiCorruption();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void validateArchitecture() {
        if (!properties.isEnabled()) {
            log.warn("⚠️ DDD架构校验已禁用！");
            return;
        }

        log.info("🚀 开始执行DDD架构校验...");
        log.info("📦 基础包路径: {}", properties.getBasePackage());

        try {
            JavaClasses importedClasses = loadClasses();

            // 按照DDD分层架构的重要性顺序执行验证
            validateDomainDrivenDesignPrinciples(importedClasses);
            validateLayeredArchitecture(importedClasses);
            validateDomainLayer(importedClasses);
            validateApplicationLayer(importedClasses);
            validateInfrastructureLayer(importedClasses);
            validateInterfacesLayer(importedClasses);
            validatePresentationLayer(importedClasses);
            validateAntiCorruptionLayer(importedClasses);
            validatePackageDependencies(importedClasses);
            validateNamingConventions(importedClasses);

            System.out.println();
            log.info("✅ DDD架构校验通过 - 代码结构符合领域驱动设计规范");
        } catch (Exception e) {
            log.error("❌ DDD架构校验失败: {}", e.getMessage());
            if (properties.isVerboseLogging()) {
                log.error("详细错误信息:", e);
            }
            if (properties.isFailOnViolation()) {
                throw new ArchitectureViolationException("DDD架构验证失败，代码结构不符合规范", e);
            }
        }
    }

    /**
     * 加载需要验证的类
     */
    private JavaClasses loadClasses() {
        if (properties.isVerboseLogging()) {
            log.debug("🔍 加载类文件进行架构分析...");
        }

        return new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(createIgnoredPackagesImportOption())
                .importPackages(properties.getBasePackage());
    }

    /**
     * 验证领域驱动设计核心原则
     */
    private void validateDomainDrivenDesignPrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            System.out.println();
            log.debug("🎯 校验DDD核心设计原则...");
        }

        // 验证聚合根设计原则
        validateAggregateRootPrinciples(classes);

        // 验证领域服务设计原则
        validateDomainServicePrinciples(classes);

        // 验证仓储模式设计原则
        validateRepositoryPatternPrinciples(classes);

        // 验证事件驱动架构原则
        validateEventDrivenArchitecturePrinciples(classes);

        // 验证聚合设计模式
        validateAggregateDesignPatterns(classes);

        // 验证值对象设计原则
        validateValueObjectPrinciples(classes);

        // 验证表现层设计原则
        validatePresentationDesignPatterns(classes);

        // 验证防腐层设计原则
        validateAntiCorruptionLayerPrinciples(classes);
    }

    /**
     * 验证聚合根设计原则
     */
    private void validateAggregateRootPrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🏛️ 验证聚合根设计原则...");
        }

        // 聚合根应该在领域层
        classes().that().haveNameMatching(".*Aggregate.*")
                .and().resideInAPackage(getDomainLayer())
                .should().resideInAPackage(getDomainLayer())
                .because("聚合根应该定义在领域层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证领域服务设计原则
     */
    private void validateDomainServicePrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("⚙️ 验证领域服务设计原则...");
        }

        // 领域服务应该在领域层的service包中
        classes().that().haveNameMatching(".*DomainService")
                .and().resideInAPackage("..domain..service..")
                .should().resideInAPackage("..domain..service..")
                .because("领域服务应该在领域层的service包中")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证仓储模式设计原则
     */
    private void validateRepositoryPatternPrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🗄️ 验证仓储模式设计原则...");
        }

        // 仓储接口应该在领域层定义
        classes().that().haveNameMatching(".*Repository")
                .and().areInterfaces()
                .and().resideInAPackage(getDomainLayer())
                .should().resideInAPackage(getDomainLayer())
                .because("仓储接口应该定义在领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 仓储实现应该在基础设施层
        classes().that().haveNameMatching(".*Repository")
                .and().areNotInterfaces()
                .and().doNotHaveSimpleName("AbstractRepository")
                .should().resideInAPackage(getInfrastructureLayer())
                .because("仓储实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 仓储实现应该实现相应的仓储接口
        classes().that().haveNameMatching(".*RepositoryImpl")
                .or().haveNameMatching(".*Jpa.*Repository")
                .should().beAssignableTo("Repository")
                .orShould().beAssignableTo("cn.treedeep.king.core.domain.AggregateRepository")
                .orShould().beAssignableTo("org.springframework.data.jpa.repository.JpaRepository")
                .because("仓储实现应该实现Repository接口")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证事件驱动架构原则
     */
    private void validateEventDrivenArchitecturePrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("📡 验证事件驱动架构原则...");
        }

        // 领域事件应该在领域层定义
        classes().that().haveNameMatching(".*DomainEvent")
                .or().haveNameMatching(".*Event")
                .and().resideInAPackage(getDomainLayer())
                .should().resideInAPackage(getDomainLayer())
                .because("领域事件应该定义在领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 事件处理器应该在应用层或基础设施层
        classes().that().haveNameMatching(".*EventHandler")
                .or().haveNameMatching(".*Handler")
                .should().resideInAnyPackage(getApplicationLayer(), getInfrastructureLayer())
                .because("事件处理器应该在应用层或基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 事件发布器实现应该在基础设施层
        classes().that().haveNameMatching(".*EventPublisher")
                .and().areNotInterfaces()
                .should().resideInAPackage(getInfrastructureLayer())
                .because("事件发布器实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 事件存储实现应该在基础设施层
        classes().that().haveNameMatching(".*EventStore")
                .and().areNotInterfaces()
                .should().resideInAPackage(getInfrastructureLayer())
                .because("事件存储实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 所有领域事件必须继承DomainEvent
        classes().that().haveNameMatching(".*DomainEvent")
                .or().haveNameMatching(".*Event")
                .and().resideInAPackage(getDomainLayer())
                .and().areNotInterfaces()
                .should().beAssignableTo("cn.treedeep.king.core.domain.DomainEvent")
                .because("所有领域事件都必须继承DomainEvent基类")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证聚合设计模式
     */
    private void validateAggregateDesignPatterns(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🧲 验证聚合设计模式...");
        }

        // 聚合根应该继承AggregateRoot基类
        classes().that().haveNameMatching(".*Aggregate")
                .and().areNotInnerClasses()
                .and().resideInAPackage(getDomainLayer())
                .should().beAssignableTo("AggregateRoot")
                .because("聚合根应该继承AggregateRoot基类")
                .allowEmptyShould(true)
                .check(classes);

        // 实体应该在聚合内部或领域层
        classes().that().haveNameMatching(".*Entity")
                .and().areNotInnerClasses()
                .and().doNotHaveSimpleName("AggregateRoot")
                .should().resideInAPackage(getDomainLayer())
                .because("实体应该在领域层定义")
                .allowEmptyShould(true)
                .check(classes);

        // 领域层不应该直接使用特定的基础设施注解
        noClasses().that().resideInAPackage(getDomainLayer())
                .should().beAnnotatedWith("org.springframework.data.jpa.repository.JpaRepository")
                .because("领域层不应该包含JPA仓储注解")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证值对象设计原则
     */
    private void validateValueObjectPrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("💎 验证值对象设计原则...");
        }

        // 值对象应该在领域层定义
        classes().that().haveNameMatching(".*Value")
                .or().haveNameMatching(".*ValueObject")
                .or().haveNameMatching(".*DomainVO")
                .or().areAnnotatedWith("jakarta.persistence.Embeddable")
                .should().resideInAPackage(getDomainLayer())
                .because("值对象应该定义在领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 不允许在值对象中使用可变的集合类型
        noClasses().that().haveNameMatching(".*Value")
                .should().dependOnClassesThat().haveNameMatching("java.util.ArrayList")
                .orShould().dependOnClassesThat().haveNameMatching("java.util.HashMap")
                .because("值对象应该避免使用可变集合")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证表现层设计原则
     */
    private void validatePresentationDesignPatterns(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🎨 验证表现层设计原则...");
        }

        // MVC控制器应该遵循命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.stereotype.Controller")
                .should().haveSimpleNameEndingWith("Controller")
                .because("MVC控制器应该以Controller结尾")
                .allowEmptyShould(true)
                .check(classes);

        // REST控制器应该遵循命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().haveSimpleNameEndingWith("Controller")
                .orShould().haveSimpleNameEndingWith("RestController")
                .because("REST控制器应该以Controller或RestController结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层组件不应该直接使用领域实体
        noClasses().that().resideInAPackage(getPresentationLayer())
                .should().dependOnClassesThat().resideInAPackage("..domain..entity..")
                .andShould().dependOnClassesThat().resideInAPackage("..domain..model..")
                .because("表现层应该使用DTO而不是直接使用领域实体")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层应该使用合适的HTTP状态码和响应格式
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().dependOnClassesThat().haveNameMatching(".*ResponseEntity.*")
                .orShould().dependOnClassesThat().haveNameMatching(".*Response.*")
                .orShould().dependOnClassesThat().haveNameMatching(".*Result.*")
                .because("REST控制器应该使用适当的响应格式")
                .allowEmptyShould(true)
                .check(classes);

        // 验证Web API的DTO命名规范
        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Request")
                .or().haveNameMatching(".*Response")
                .or().haveNameMatching(".*ViewModel")
                .or().haveNameMatching(".*WebDTO")
                .or().haveNameMatching(".*PresentationDTO")
                .or().haveNameMatching(".*Form")
                .should().resideInAPackage(getPresentationLayer())
                .because("Web API的数据传输对象应该在表现层定义")
                .allowEmptyShould(true)
                .check(classes);

        // 基础输入校验应该在表现层
        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Validator")
                .or().areAnnotatedWith("org.springframework.validation.Validator")
                .should().resideInAPackage(getPresentationLayer())
                .because("基础输入校验应该在表现层处理")
                .allowEmptyShould(true)
                .check(classes);

        // 路由配置应该在表现层
        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Route.*")
                .or().haveNameMatching(".*Mapping.*")
                .should().resideInAPackage(getPresentationLayer())
                .because("路由配置应该在表现层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 验证防腐层设计原则
     */
    private void validateAntiCorruptionLayerPrinciples(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🛡️ 验证防腐层设计原则...");
        }

        // 防腐层适配器应该实现依赖倒置原则
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Adapter")
                .and().areNotInterfaces()
                .should().beAssignableTo("Adapter")
                .orShould().haveSimpleNameEndingWith("Adapter")
                .because("防腐层适配器应该遵循适配器模式")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层转换器应该遵循命名规范
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Translator")
                .or().haveNameMatching(".*Converter")
                .should().haveSimpleNameEndingWith("Translator")
                .orShould().haveSimpleNameEndingWith("Converter")
                .because("防腐层转换器应该遵循命名规范")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层组件应该处理外部系统调用
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().areNotInterfaces()
                .should().accessClassesThat().haveNameMatching(".*Client")
                .orShould().accessClassesThat().haveNameMatching(".*Api")
                .because("防腐层应该处理外部系统调用")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层网关应该封装外部API调用
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Gateway")
                .and().areNotInterfaces()
                .should().accessClassesThat().haveNameMatching(".*Client")
                .orShould().accessClassesThat().haveNameMatching(".*Api")
                .because("防腐层网关应该封装外部API调用")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层不应该暴露外部技术细节到领域层
        noClasses().that().resideInAPackage(getDomainLayer())
                .should().dependOnClassesThat().resideInAPackage("..external..")
                .because("领域层不应该依赖外部技术细节，应通过防腐层隔离")
                .allowEmptyShould(true)
                .check(classes);
    }


    /**
     * 定义基于DDD的分层架构规则
     * <p>
     * 该规则强制各层级之间的依赖约束，确保架构清晰和关注点分离
     *
     * <h5>层级定义说明：</b></p>
     * <ul>
     *   <li><b>Domain</b> - 领域层（核心业务逻辑，含实体、值对象、领域服务）</li>
     *   <li><b>Application</b> - 应用层（用例编排，处理Command/Query）</li>
     *   <li><b>Infrastructure</b> - 基础设施层（数据库、消息队列等技术支持）</li>
     *   <li><b>Interfaces</b> - 接口层（系统对外交互，含API/消息监听等）</li>
     *   <li><b>Presentation</b> - 展现层（用户交互，如Controller/DTO转换）</li>
     *   <li><b>AntiCorruption</b> - 防腐层（隔离外部系统，防止外部模型污染）</li>
     *   <li><b>Shared</b> - 共享层（通用工具类/常量）</li>
     * </ul>
     *
     * <h5>依赖规则说明：</b></p>
     * <ol>
     *   <li>展现层(Presentation)只能访问：应用层(Application)、共享层(Shared)</li>
     *   <li>接口层(Interfaces)只能访问：应用层、展现层、共享层</li>
     *   <li>应用层(Application)只能访问：领域层(Domain)、共享层、防腐层(AntiCorruption)</li>
     *   <li>领域层(Domain)只能访问：共享层（禁止依赖技术实现）</li>
     *   <li>防腐层(AntiCorruption)只能访问：领域层、共享层</li>
     *   <li>基础设施层(Infrastructure)可被所有层访问（通过依赖倒置）</li>
     * </ol>
     *
     * <h5>典型违规示例：</b></p>
     * <pre>{@code
     * // 错误：展现层直接访问领域层
     * @Controller
     * public class UserController {
     *     private final UserRepository repo; // 应改为访问ApplicationService
     * }
     * }</pre>
     *
     * @see ArchRule#allowEmptyShould(boolean) 允许空校验规则
     * @since 1.0
     */
    private void validateLayeredArchitecture(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("😊 校验分层架构约束...");
        }

        // 分层架构规则
        ArchRule layeredArchitectureRule = layeredArchitecture()
                // 包扫描范围设置
                .consideringOnlyDependenciesInAnyPackage(properties.getBasePackage() + "..")

                // 层级定义
                .layer("Domain").definedBy(getDomainLayer())
                .layer("Application").definedBy(getApplicationLayer())
                .layer("Infrastructure").definedBy(getInfrastructureLayer())
                .layer("Interfaces").definedBy(getInterfacesLayer())
                .layer("Presentation").definedBy(getPresentationLayer())
                .layer("Shared").definedBy(getSharedLayer())
                .layer("AntiCorruption").definedBy(getAntiCorruptionLayer())

                // 依赖约束规则
                .whereLayer("Presentation").mayOnlyAccessLayers("Application", "Shared")
                .whereLayer("Interfaces").mayOnlyAccessLayers("Application", "Presentation", "Shared")
                .whereLayer("Application").mayOnlyAccessLayers("Domain", "Shared", "AntiCorruption")
                .whereLayer("Domain").mayOnlyAccessLayers("Shared")
                .whereLayer("AntiCorruption").mayOnlyAccessLayers("Domain", "Shared")
                .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Domain", "Application", "Interfaces", "Presentation", "Shared", "AntiCorruption")
                .allowEmptyShould(true);

        layeredArchitectureRule.check(classes);
    }

    /**
     * 校验领域层约束
     */
    private void validateDomainLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🤔 校验领域层约束...");
        }

        // 领域层不应该依赖基础设施层
        noClasses().that().resideInAPackage(getDomainLayer())
                .should().dependOnClassesThat().resideInAPackage(getInfrastructureLayer())
                .because("领域层不应该依赖基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 领域层不应该依赖应用层
        noClasses().that().resideInAPackage(getDomainLayer())
                .should().dependOnClassesThat().resideInAPackage(getApplicationLayer())
                .because("领域层不应该依赖应用层")
                .allowEmptyShould(true)
                .check(classes);

        // 领域层不应该依赖接口层
        noClasses().that().resideInAPackage(getDomainLayer())
                .should().dependOnClassesThat().resideInAPackage(getInterfacesLayer())
                .because("领域层不应该依赖接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 领域仓储接口应该在领域层定义，基础设施仓储实现应该在基础设施层
        classes().that().resideInAPackage(getDomainLayer())
                .and().haveNameMatching(".*Repository")
                .and().areInterfaces()
                .should().resideInAPackage(getDomainLayer())
                .because("领域仓储接口应该定义在领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 仓储实现应该在基础设施层
        classes().that().haveNameMatching(".*Repository.*Impl")
                .or().haveNameMatching(".*Jpa.*Repository")
                .should().resideInAPackage(getInfrastructureLayer())
                .because("仓储实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验应用层约束
     */
    private void validateApplicationLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("😄 校验应用层约束...");
        }


        // 应用层命令和查询DTO应该在应用层定义
        classes().that().resideInAPackage(getApplicationLayer())
                .and().haveNameMatching(".*Command")
                .or().haveNameMatching(".*Query")
                .or().haveNameMatching(".*AppDTO")
                .or().haveNameMatching(".*ApplicationDTO")
                .or().haveNameMatching(".*Result")
                .should().resideInAPackage(getApplicationLayer())
                .because("应用层的Command、Query和结果DTO应该在应用层定义")
                .allowEmptyShould(true)
                .check(classes);

        // 应用层不应该依赖接口层
        noClasses().that().resideInAPackage(getApplicationLayer())
                .should().dependOnClassesThat().resideInAPackage(getInterfacesLayer())
                .because("应用层不应该依赖接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 应用服务应该使用@Service注解（如果存在的话）
        classes().that().haveNameMatching(".*ApplicationServiceImpl")
                .or().haveNameMatching(".*AppServiceImpl")
                .should().beAnnotatedWith("org.springframework.stereotype.Service")
                .because("应用服务应该使用@Service注解")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验基础设施层约束
     */
    private void validateInfrastructureLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("😐 校验基础设施层约束...");
        }

        // 仓储实现应该在基础设施层（如果存在的话）
        classes().that().haveNameMatching(".*RepositoryImpl")
                .should().resideInAPackage(getInfrastructureLayer())
                .because("仓储实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 事件总线实现应该在基础设施层（如果存在的话）
        classes().that().haveNameMatching(".*EventBus")
                .and().areNotInterfaces()
                .should().resideInAPackage(getInfrastructureLayer())
                .because("事件总线实现应该在基础设施层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验接口层约束
     */
    private void validateInterfacesLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🔌 校验接口层约束...");
        }

        // RPC服务接口应该在接口层
        classes().that().haveNameMatching(".*RpcService")
                .or().haveNameMatching(".*RemoteService")
                .or().haveNameMatching(".*ApiClient")
                .should().resideInAPackage(getInterfacesLayer())
                .because("RPC服务接口应该在接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 消息队列处理器应该在接口层
        classes().that().areAnnotatedWith("org.springframework.amqp.rabbit.annotation.RabbitListener")
                .or().areAnnotatedWith("org.springframework.kafka.annotation.KafkaListener")
                .or().haveNameMatching(".*MessageListener")
                .or().haveNameMatching(".*MessageHandler")
                .should().resideInAPackage(getInterfacesLayer())
                .because("消息队列处理器应该在接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 协议集成组件应该在接口层（纯协议转换，不涉及外部系统防腐）
        classes().that().haveNameMatching(".*Integration")
                .and().areNotInnerClasses()
                .and().resideInAPackage("..interfaces..")
                .should().resideInAPackage(getInterfacesLayer())
                .because("协议集成组件应该在接口层")
                .allowEmptyShould(true)
                .check(classes);

        // API客户端应该在接口层
        classes().that().haveNameMatching(".*ApiClient")
                .or().haveNameMatching(".*RemoteClient")
                .should().resideInAPackage(getInterfacesLayer())
                .because("API客户端应该在接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 系统间通信的DTO应该在接口层
        classes().that().resideInAPackage(getInterfacesLayer())
                .and().haveNameMatching(".*Message")
                .or().haveNameMatching(".*IntegrationEvent")
                .or().haveNameMatching(".*ExternalEvent")
                .or().haveNameMatching(".*RemoteCommand")
                .or().haveNameMatching(".*RemoteQuery")
                .or().haveNameMatching(".*IntegrationDTO")
                .or().haveNameMatching(".*ExternalDTO")
                .or().haveNameMatching(".*ApiRequest")
                .or().haveNameMatching(".*ApiResponse")
                .should().resideInAPackage(getInterfacesLayer())
                .because("系统间通信的DTO应该在接口层")
                .allowEmptyShould(true)
                .check(classes);

        // 接口层不应该直接依赖领域层实体
        noClasses().that().resideInAPackage(getInterfacesLayer())
                .should().dependOnClassesThat().resideInAPackage("..domain..entity..")
                .andShould().dependOnClassesThat().resideInAPackage("..domain..model..")
                .because("接口层不应该直接依赖领域实体")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验表现层约束
     */
    private void validatePresentationLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🧑‍🎨 校验表现层约束...");
        }

        // Web控制器应该在表现层
        classes().that().areAnnotatedWith("org.springframework.stereotype.Controller")
                .or().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().resideInAPackage(getPresentationLayer())
                .orShould().resideInAPackage(getInterfacesLayer())
                .because("Web控制器应该在表现层")
                .allowEmptyShould(true)
                .check(classes);

        // Web API特定的DTO应该在表现层
        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Request")
                .or().haveNameMatching(".*Response")
                .or().haveNameMatching(".*ViewModel")
                .or().haveNameMatching(".*Form")
                .or().haveNameMatching(".*WebDTO")
                .or().haveNameMatching(".*PresentationDTO")
                .should().resideInAPackage(getPresentationLayer())
                .because("Web API特定的数据传输对象应该在表现层定义")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层不应该直接依赖领域层实体
        noClasses().that().resideInAPackage(getPresentationLayer())
                .should().dependOnClassesThat().resideInAPackage("..domain..model..")
                .andShould().dependOnClassesThat().resideInAPackage("..domain..entity..")
                .because("表现层不应该直接依赖领域实体，应通过DTO传输数据")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层不应该直接依赖基础设施层
        noClasses().that().resideInAPackage(getPresentationLayer())
                .should().dependOnClassesThat().resideInAPackage(getInfrastructureLayer())
                .because("表现层不应该直接依赖基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // Web配置类应该在表现层
        classes().that().areAnnotatedWith("org.springframework.web.servlet.config.annotation.WebMvcConfigurer")
                .or().haveNameMatching(".*WebConfig")
                .or().haveNameMatching(".*WebMvcConfig")
                .or().haveNameMatching(".*CorsConfig")
                .should().resideInAPackage(getPresentationLayer())
                .because("Web配置类应该在表现层")
                .allowEmptyShould(true)
                .check(classes);

        // 异常处理器应该在表现层
        classes().that().areAnnotatedWith("org.springframework.web.bind.annotation.ControllerAdvice")
                .or().areAnnotatedWith("org.springframework.web.bind.annotation.RestControllerAdvice")
                .should().resideInAPackage(getPresentationLayer())
                .because("Web异常处理器应该在表现层")
                .allowEmptyShould(true)
                .check(classes);

        // 验证器应该在表现层
        classes().that().areAnnotatedWith("org.springframework.validation.Validator")
                .or().haveNameMatching(".*Validator")
                .and().resideInAPackage(getPresentationLayer())
                .should().resideInAPackage(getPresentationLayer())
                .because("Web输入验证器应该在表现层")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层组件命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.stereotype.Controller")
                .should().haveSimpleNameEndingWith("Controller")
                .because("表现层控制器应该以Controller结尾")
                .allowEmptyShould(true)
                .check(classes);

        // REST API应该有适当的HTTP状态码处理
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().dependOnClassesThat().haveNameMatching(".*ResponseEntity.*")
                .orShould().dependOnClassesThat().haveNameMatching(".*HttpStatus.*")
                .because("REST API应该使用适当的HTTP状态码")
                .allowEmptyShould(true)
                .check(classes);

        // 路由配置应该在表现层
        classes().that().haveNameMatching(".*Route.*")
                .or().haveNameMatching(".*Mapping.*")
                .and().resideInAPackage(getPresentationLayer())
                .should().resideInAPackage(getPresentationLayer())
                .because("路由配置应该在表现层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验防腐层约束
     */
    private void validateAntiCorruptionLayer(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("🛡️ 校验防腐层约束...");
        }

        // 防腐层应该在基础设施层的acl包中
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .should().resideInAPackage("..infrastructure..acl..")
                .because("防腐层应该在基础设施层的acl包中")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层适配器应该遵循命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Adapter")
                .should().haveSimpleNameEndingWith("Adapter")
                .because("防腐层适配器应该以Adapter结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层转换器应该遵循命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Translator")
                .should().haveSimpleNameEndingWith("Translator")
                .orShould().haveSimpleNameEndingWith("Converter")
                .because("防腐层转换器应该以Translator或Converter结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层网关应该遵循命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Gateway")
                .should().haveSimpleNameEndingWith("Gateway")
                .because("防腐层网关应该以Gateway结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层客户端应该遵循命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Client")
                .should().haveSimpleNameEndingWith("Client")
                .orShould().haveSimpleNameEndingWith("Service")
                .because("防腐层客户端应该以Client或Service结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层组件不应该直接暴露外部模型给领域层
        noClasses().that().resideInAPackage(getAntiCorruptionLayer())
                .should().dependOnClassesThat().resideInAPackage("..domain..")
                .andShould().haveNameMatching(".*External.*")
                .because("防腐层不应该将外部模型暴露给领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层实现应该使用Spring注解
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().haveNameMatching(".*Impl")
                .should().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
                .because("防腐层实现类应该使用@Component或@Service注解")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层接口应该在领域层定义
        classes().that().areInterfaces()
                .and().haveNameMatching(".*Gateway")
                .or().haveNameMatching(".*ExternalService")
                .or().haveNameMatching(".*ThirdPartyService")
                .should().resideInAPackage(getDomainLayer())
                .because("防腐层接口应该定义在领域层，实现在基础设施层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层不应该让外部异常泄露到领域层
        noClasses().that().resideInAPackage(getAntiCorruptionLayer())
                .should().dependOnClassesThat().haveNameMatching(".*Exception")
                .andShould().dependOnClassesThat().resideInAPackage("..external..")
                .because("防腐层应该转换外部异常，不能让其泄露到领域层")
                .allowEmptyShould(true)
                .check(classes);

        // 验证外部系统集成组件在防腐层（涉及外部系统的防腐处理）
        classes().that().haveNameMatching(".*Integration")
                .and().resideInAPackage("..acl..")
                .or().haveNameMatching(".*ExternalApi")
                .or().haveNameMatching(".*ThirdParty.*")
                .should().resideInAPackage(getAntiCorruptionLayer())
                .because("外部系统集成组件应该在防腐层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层专有组件验证
        classes().that().haveNameMatching(".*Gateway")
                .or().haveNameMatching(".*Adapter")
                .or().haveNameMatching(".*ExternalService")
                .or().haveNameMatching(".*ThirdPartyService")
                .should().resideInAPackage(getAntiCorruptionLayer())
                .because("Gateway、Adapter、ExternalService等组件应该在防腐层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层数据传输对象应该独立于外部模型
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*ExternalDTO")
                .or().haveNameMatching(".*ThirdPartyDTO")
                .or().haveNameMatching(".*AdapterDTO")
                .or().haveNameMatching(".*GatewayDTO")
                .or().haveNameMatching(".*ExternalData")
                .should().resideInAPackage(getAntiCorruptionLayer())
                .because("防腐层外部数据传输对象应该在防腐层内部定义")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层配置应该集中管理
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Config")
                .or().haveNameMatching(".*Configuration")
                .should().beAnnotatedWith("org.springframework.context.annotation.Configuration")
                .because("防腐层配置类应该使用@Configuration注解")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层不应该直接依赖应用层
        noClasses().that().resideInAPackage(getAntiCorruptionLayer())
                .should().dependOnClassesThat().resideInAPackage(getApplicationLayer())
                .because("防腐层不应该直接依赖应用层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层不应该直接依赖表现层
        noClasses().that().resideInAPackage(getAntiCorruptionLayer())
                .should().dependOnClassesThat().resideInAPackage(getPresentationLayer())
                .because("防腐层不应该直接依赖表现层")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层不应该直接依赖接口层
        noClasses().that().resideInAPackage(getAntiCorruptionLayer())
                .should().dependOnClassesThat().resideInAPackage(getInterfacesLayer())
                .because("防腐层不应该直接依赖接口层")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验包依赖关系 - 避免循环依赖
     */
    private void validatePackageDependencies(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("😰 校验包依赖关系...");
        }

        slices().matching(properties.getBasePackage() + ".(*)..")
                .should().beFreeOfCycles()
                .because("包之间不应该存在循环依赖")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 校验命名约定
     * <p>
     * <b>DTO分层命名约定说明：</b>
     * <ul>
     *   <li><b>领域层</b>：*Value、*ValueObject、*DomainVO、*Event、*DomainEvent - 领域值对象和领域事件</li>
     *   <li><b>应用层</b>：*Command、*Query、*AppDTO、*ApplicationDTO、*Result - 应用层业务对象</li>
     *   <li><b>表现层</b>：*Request、*Response、*ViewModel、*Form、*WebDTO、*PresentationDTO - Web API数据传输</li>
     *   <li><b>接口层</b>：*Message、*IntegrationEvent、*ExternalEvent、*RemoteCommand、*RemoteQuery、*IntegrationDTO、*ExternalDTO、*ApiRequest、*ApiResponse - 系统间通信</li>
     *   <li><b>防腐层</b>：*ExternalDTO、*ThirdPartyDTO、*AdapterDTO、*GatewayDTO、*ExternalData - 外部系统数据转换</li>
     * </ul>
     * <p>
     * 这种命名约定避免了不同层级间的冲突，确保了架构的清晰性。
     */
    private void validateNamingConventions(JavaClasses classes) {
        if (properties.isVerboseLogging()) {
            log.debug("😋 校验命名约定...");
        }

        // 领域服务命名约定（如果存在的话）
        classes().that().resideInAPackage("..domain..service..")
                .and().areNotInterfaces()
                .should().haveSimpleNameEndingWith("Service")
                .orShould().haveSimpleNameEndingWith("Impl")
                .because("领域服务应该以Service或Impl结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 应用服务命名约定（如果存在的话）
        classes().that().resideInAPackage("..application..service..")
                .should().haveSimpleNameEndingWith("Service")
                .orShould().haveSimpleNameEndingWith("ApplicationService")
                .orShould().haveSimpleNameEndingWith("Impl")
                .because("应用服务应该以Service或ApplicationService以及Impl结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层控制器命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.stereotype.Controller")
                .should().haveSimpleNameEndingWith("Controller")
                .because("表现层控制器应该以Controller结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层REST控制器命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().haveSimpleNameEndingWith("Controller")
                .orShould().haveSimpleNameEndingWith("RestController")
                .because("表现层REST控制器应该以Controller或RestController结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 表现层DTO命名约定
        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Request")
                .or().haveNameMatching(".Request*")
                .should().haveSimpleNameEndingWith("Request")
                .orShould().haveSimpleNameStartingWith("Request")
                .because("请求DTO应该以Request结尾或开头")
                .allowEmptyShould(true)
                .check(classes);

        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*Response")
                .or().haveNameMatching(".Response*")
                .or().haveNameMatching(".Result")
                .should().haveSimpleNameEndingWith("Response")
                .orShould().haveSimpleNameStartingWith("Response")
                .orShould().haveSimpleNameStartingWith("Result")
                .because("响应DTO应该以Response结尾或开头，或Result结尾")
                .allowEmptyShould(true)
                .check(classes);

        classes().that().resideInAPackage(getPresentationLayer())
                .and().haveNameMatching(".*ViewModel")
                .should().haveSimpleNameEndingWith("ViewModel")
                .orShould().haveSimpleNameEndingWith("VM")
                .because("视图模型应该以ViewModel或VM结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层组件命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Adapter")
                .should().haveSimpleNameEndingWith("Adapter")
                .because("防腐层适配器应该以Adapter结尾")
                .allowEmptyShould(true)
                .check(classes);

        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Gateway")
                .should().haveSimpleNameEndingWith("Gateway")
                .because("防腐层网关应该以Gateway结尾")
                .allowEmptyShould(true)
                .check(classes);

        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Translator")
                .or().haveNameMatching(".*Converter")
                .should().haveSimpleNameEndingWith("Translator")
                .orShould().haveSimpleNameEndingWith("Converter")
                .because("防腐层转换器应该以Translator或Converter结尾")
                .allowEmptyShould(true)
                .check(classes);

        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Facade")
                .should().haveSimpleNameEndingWith("Facade")
                .because("防腐层外观应该以Facade结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 接口层组件命名约定
        classes().that().resideInAPackage(getInterfacesLayer())
                .and().haveNameMatching(".*Integration")
                .should().haveSimpleNameEndingWith("Integration")
                .because("接口层集成组件应该以Integration结尾")
                .allowEmptyShould(true)
                .check(classes);

        // 防腐层集成组件命名约定
        classes().that().resideInAPackage(getAntiCorruptionLayer())
                .and().haveNameMatching(".*Integration")
                .should().haveSimpleNameEndingWith("Integration")
                .because("防腐层集成组件应该以Integration结尾")
                .allowEmptyShould(true)
                .check(classes);
    }

    /**
     * 创建忽略包的导入选项
     */
    private ImportOption createIgnoredPackagesImportOption() {
        return location -> {
            String locationPath = location.toString();
            String path = location.asURI().getPath();

            // 忽略所有 package-info.class 文件
            if (path.endsWith("package-info.class")) {
                return false; // 忽略 package-info.java 文件
            }

            String[] ignoredPackages = properties.getIgnoredPackages();
            if (ignoredPackages == null) {
                return true; // 如果没有配置忽略包，则包含所有包
            }

            // 检查是否匹配任何忽略的包模式
            for (String ignoredPackage : ignoredPackages) {
                if (matchesPackagePattern(locationPath, ignoredPackage)) {
                    if (properties.isVerboseLogging() && properties.isLogShowIgnoredClass()) {
                        log.debug("忽略：{} -> {}", path.substring(path.lastIndexOf("/") + 1), ignoredPackage);
                    }
                    return false; // 忽略此包
                }
            }

            return true; // 包含此包
        };
    }

    /**
     * 检查路径是否匹配包模式
     */
    private boolean matchesPackagePattern(String locationPath, String pattern) {
        // 将文件路径转换为包名格式进行匹配
        String packagePath = locationPath.replace("/", ".").replace("\\", ".");

        // 如果是精确包名模式（不含通配符）
        if (!pattern.contains("..") && !pattern.contains("*")) {
            // 精确匹配包名或其子包
            return packagePath.contains(pattern + ".") || packagePath.contains("." + pattern + ".")
                    || packagePath.endsWith("." + pattern) || packagePath.equals(pattern);
        }

        // 处理通配符模式
        String regex = pattern
                .replace(".", "\\.")      // 转义点号
                .replace("\\.\\.", ".*")  // 将 .. 转换为 .*（注意顺序）
                .replace("*", "[^.]*");   // 将 * 转换为 [^.]*

        // 检查包路径是否匹配模式
        return packagePath.matches(".*" + regex + ".*");
    }

}
