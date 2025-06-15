package cn.treedeep.king.generator.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 模块配置数据传输对象
 * <p>
 * 用于JSON序列化和反序列化的数据结构
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Getter
@Setter
public class ModuleConfigDto {
    
    /**
     * 模块名称
     */
    private String name;
    
    /**
     * 模块注释
     */
    private String comment;
    
    /**
     * 聚合根列表
     */
    private List<AggregateRootDto> aggregateRoots;
    
    /**
     * 领域事件列表
     */
    private List<DomainEventDto> domainEvents;
    
    /**
     * 应用服务列表
     */
    private List<ApplicationServiceDto> applicationServices;

    /**
     * 聚合根配置DTO
     */
    @Getter
    @Setter
    public static class AggregateRootDto {
        private String name;
        private String comment;
        private List<PropertyDto> properties;
        private List<EntityDto> entities;
        private List<ValueObjectDto> valueObjects;
        private List<MethodDto> methods;
    }

    /**
     * 属性配置DTO
     */
    @Getter
    @Setter
    public static class PropertyDto {
        private String name;
        private String comment;
        
        /**
         * 属性类型标识，用于区分不同类型的属性以便正确生成代码
         * <p>
         * 可选值：
         * <ul>
         *   <li><b>"REGULAR"</b> - 普通属性
         *       <ul>
         *         <li>含义：普通的业务属性，直接存储为数据库字段</li>
         *         <li>生成结果：在实体或聚合根中生成普通的属性字段</li>
         *         <li>示例：username、email、status 等基础数据字段</li>
         *         <li>数据库映射：直接作为表字段存储</li>
         *       </ul>
         *   </li>
         *   <li><b>"VALUE_OBJECT_PROPERTY"</b> - 值对象属性
         *       <ul>
         *         <li>含义：实体中引用值对象的属性，用于嵌套值对象</li>
         *         <li>生成结果：在实体中使用 @Embedded 注解嵌入值对象</li>
         *         <li>示例：实体中的 deviceFingerprint、address 等复杂值对象</li>
         *         <li>数据库映射：值对象的属性会被展开到当前实体表中</li>
         *       </ul>
         *   </li>
         *   <li><b>"AGGREGATE_ROOT_PROPERTY"</b> - 聚合根属性
         *       <ul>
         *         <li>含义：聚合根中引用值对象的属性，用于在聚合根层面嵌入值对象</li>
         *         <li>生成结果：在聚合根中使用 @Embedded 注解嵌入值对象</li>
         *         <li>示例：聚合根中的 phone、address 等需要嵌入的值对象</li>
         *         <li>数据库映射：值对象属性会直接嵌入到聚合根表中</li>
         *       </ul>
         *   </li>
         * </ul>
         * <p>
         * 对应的Java类型映射：
         * <ul>
         *   <li>REGULAR → Property</li>
         *   <li>VALUE_OBJECT_PROPERTY → Property.ValueObjectProperty</li>
         *   <li>AGGREGATE_ROOT_PROPERTY → Property.AggregateRootProperty</li>
         * </ul>
         */
        private String type;
    }

    /**
     * 实体配置DTO
     */
    @Getter
    @Setter
    public static class EntityDto {
        private String name;
        private String comment;
        private List<PropertyDto> properties;
    }

    /**
     * 值对象配置DTO
     */
    @Getter
    @Setter
    public static class ValueObjectDto {
        private String name;
        private String comment;
        private List<PropertyDto> properties;
    }

    /**
     * 方法配置DTO
     */
    @Getter
    @Setter
    public static class MethodDto {
        private String name;
        private String comment;
        private String returnType;
        private List<ParameterDto> parameters;
    }

    /**
     * 参数配置DTO
     */
    @Getter
    @Setter
    public static class ParameterDto {
        private String name;
        private String type;
        private String comment;
    }

    /**
     * 领域事件配置DTO
     */
    @Getter
    @Setter
    public static class DomainEventDto {
        private String name;
        private String comment;
        private String aggregateRootName;
        private String tableName;
        private List<EventFieldDto> fields;
    }

    /**
     * 事件字段配置DTO
     */
    @Getter
    @Setter
    public static class EventFieldDto {
        private String name;
        private String type;
        private String comment;
        private String columnName;
    }

    /**
     * 应用服务配置DTO
     */
    @Getter
    @Setter
    public static class ApplicationServiceDto {
        private String name;
        private String comment;
        private String moduleName;
        private String interfaceName;
        private String implementationName;
        private List<ServiceMethodDto> methods;
    }

    /**
     * 服务方法配置DTO
     */
    @Getter
    @Setter
    public static class ServiceMethodDto {
        private String name;
        private String comment;
        private String returnType;
        private List<ParameterDto> parameters;
    }
}
