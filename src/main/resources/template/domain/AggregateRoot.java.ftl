package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.AggregateRoot;
import ${packageName}.${moduleNameLower}.domain.event.SayHelloEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import static java.sql.DriverManager.println;

<#-- 根据属性添加必要的import -->
<#assign needTimeImport = false>
<#if aggregateProperties?has_content>
<#list aggregateProperties as property>
<#if property.name?contains("Time") || property.name?contains("Date") || property.name?contains("At")>
<#assign needTimeImport = true>
<#break>
</#if>
</#list>
</#if>
<#if needTimeImport>
import java.time.OffsetDateTime;
</#if>

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「聚合根」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${entityTableName}s")
@Comment("${entityComment}表")
@Getter
@Setter
public class ${entityNameCamel} extends AggregateRoot<${entityNameCamel}Id> {
    
    public ${entityNameCamel}() {
        super();
    }

    @EmbeddedId
    private ${entityNameCamel}Id id;

<#-- 值对象属性使用@Embedded注解 -->
<#if valueObjectProperties?has_content>
<#list valueObjectProperties as property>
    @Embedded
    @Comment("${property.comment}")
    private ${property.name?cap_first} ${property.name?uncap_first};

</#list>
</#if>

<#-- 聚合根自身属性使用普通字段 -->
<#if aggregateProperties?has_content>
<#list aggregateProperties as property>
    @Comment("${property.comment}")
    private <#if property.name?contains("Time") || property.name?contains("Date") || property.name?contains("At")>OffsetDateTime<#else>String</#if> ${property.name};

</#list>
</#if>

    public void sayHello(String name) {
        println("聚合根：Hello, " + name);
        this.registerEvent(new SayHelloEvent(this.id, name));
    }

<#-- 生成领域方法 -->
<#if domainMethods?has_content>
<#list domainMethods as method>
    /**
     * ${method.comment}
<#if method.parameters?has_content>
<#list method.parameters as param>
     * @param ${param.name} ${param.comment}
</#list>
</#if>
<#if method.returnType != "void">
     * @return ${method.returnType}
</#if>
     */
    public ${method.returnType} ${method.name}(<#if method.parameters?has_content><#list method.parameters as param>${param.type} ${param.name}<#if param_has_next>, </#if></#list></#if>) {
        // TODO 实现${method.comment}
<#if method.returnType != "void">
        <#if method.returnType == "String">
        return "";
        <#elseif method.returnType == "boolean">
        return false;
        <#elseif method.returnType == "int" || method.returnType == "Integer">
        return 0;
        <#elseif method.returnType == "long" || method.returnType == "Long">
        return 0L;
        <#elseif method.returnType == "double" || method.returnType == "Double">
        return 0.0;
        <#else>
        return null;
        </#if>
</#if>
    }

</#list>
</#if>
    // TODO 添加${entityComment}聚合的业务逻辑
}
