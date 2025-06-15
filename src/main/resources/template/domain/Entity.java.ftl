package ${packageName}.${moduleNameLower}.domain;

import cn.treedeep.king.core.domain.EntityBase;
import cn.treedeep.king.core.domain.UIdentifier;
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

<#if properties?has_content>
<#list properties as property>
    @Comment("${property.comment}")
    private <#if property.name?contains("Time") || property.name?contains("Date")>OffsetDateTime<#else>String</#if> ${property.name};

</#list>

    public ${entityNameCamel}(<#list properties as property><#if property.name?contains("Time") || property.name?contains("Date")>OffsetDateTime<#else>String</#if> ${property.name}<#if property?has_next>, </#if></#list>) {
        super();
        this.id = new UIdentifier();
<#list properties as property>
        this.${property.name} = ${property.name};
</#list>
    }
<#else>
    @Comment("名称")
    private String name;

    public ${entityNameCamel}(String name) {
        super();
        this.id = new UIdentifier();
        this.name = name;
    }
</#if>

    // TODO 添加${entityComment}实体范畴内的方法
}
