package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.EntityBase;
import cn.treedeep.king.core.domain.UIdentifier;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

<#-- 根据属性添加必要的import -->
<#if properties?has_content>
<#list properties as property>
<#if property.name?contains("Time") || property.name?contains("Date") || property.name?contains("At")>
import java.time.OffsetDateTime;
<#break>
</#if>
</#list>
</#if>

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「实体」
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
@NoArgsConstructor
public class ${entityNameCamel} extends EntityBase<UIdentifier> {

    @EmbeddedId
    @Comment("ID")
    private UIdentifier id;

<#-- 普通属性 -->
<#if regularProperties?has_content>
<#list regularProperties as property>
    @Comment("${property.comment}")
    private <#if property.name?contains("Time") || property.name?contains("Date")>OffsetDateTime<#else>String</#if> ${property.name};

</#list>
</#if>

<#-- 值对象属性使用@Embedded注解 -->
<#if valueObjectProperties?has_content>
<#list valueObjectProperties as property>
    @Embedded
    @Comment("${property.comment}")
    private ${property.name?cap_first} ${property.name?uncap_first};

</#list>
</#if>

<#-- 如果没有任何属性，添加默认name属性 -->
<#if !regularProperties?has_content && !valueObjectProperties?has_content>
    @Comment("名称")
    private String name;

</#if>

<#-- 构造函数 -->
<#if regularProperties?has_content || valueObjectProperties?has_content>
    public ${entityNameCamel}(<#list regularProperties as property><#if property.name?contains("Time") || property.name?contains("Date")>OffsetDateTime<#else>String</#if> ${property.name}<#if property?has_next || valueObjectProperties?has_content>, </#if></#list><#list valueObjectProperties as property>${property.name?cap_first} ${property.name?uncap_first}<#if property?has_next>, </#if></#list>) {
        super();
        this.id = new UIdentifier();
<#list regularProperties as property>
        this.${property.name} = ${property.name};
</#list>
<#list valueObjectProperties as property>
        this.${property.name?uncap_first} = ${property.name?uncap_first};
</#list>
    }
<#else>
    public ${entityNameCamel}(String name) {
        super();
        this.id = new UIdentifier();
        this.name = name;
    }
</#if>

    // TODO 添加${entityComment}实体范畴内的方法
}
