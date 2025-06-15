package ${packageName}.${moduleNameLower}.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
<#if aggregateRootClass?has_content>
import ${packageName}.${moduleNameLower}.domain.${aggregateRootClass}Id;
</#if>
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

<#-- 根据字段类型添加必要的import -->
<#assign needTimeImport = false>
<#if eventFields?has_content>
<#list eventFields as field>
<#if field.type?contains("OffsetDateTime") || field.type?contains("LocalDateTime") || field.type?contains("LocalDate")>
<#assign needTimeImport = true>
<#break>
</#if>
</#list>
</#if>
<#if needTimeImport>
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
</#if>

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${eventName}「领域事件」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Entity
@Table(name = "${tableName}")
@Comment("${eventComment}事件表")
@Getter
@Setter
public class ${eventName} extends DomainEvent {

<#-- 生成事件字段 -->
<#if eventFields?has_content>
<#list eventFields as field>
    @Column(name = "${field.columnName}")
    @Comment("${field.comment}")
    private ${field.type} ${field.name};

</#list>
</#if>

    /**
     * 构造函数
<#if aggregateRootClass?has_content>
     * @param aggregateId 聚合根ID
</#if>
<#if eventFields?has_content>
<#list eventFields as field>
     * @param ${field.name} ${field.comment}
</#list>
</#if>
     */
    public ${eventName}(<#if aggregateRootClass?has_content>${aggregateRootClass}Id aggregateId<#if eventFields?has_content>, </#if></#if><#if eventFields?has_content><#list eventFields as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list></#if>) {
<#if aggregateRootClass?has_content>
        super(aggregateId);
        setAggregateId(aggregateId.getValue());
<#else>
        super();
</#if>
<#if eventFields?has_content>
<#list eventFields as field>
        this.${field.name} = ${field.name};
</#list>
</#if>
    }
}
