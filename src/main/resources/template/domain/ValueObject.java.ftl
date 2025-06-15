package ${packageName}.${moduleNameLower}.domain;

<#if properties?has_content>
<#list properties as property>
<#if property.name?contains("Id") || property.name?contains("ID")>
import cn.treedeep.king.core.domain.UIdentifier;
<#break>
</#if>
</#list>
</#if>
import cn.treedeep.king.core.domain.ValueObjectBase;
<#if properties?has_content && (properties?size > 1)>
import cn.treedeep.king.shared.utils.JsonUtil;
import jakarta.persistence.AttributeConverter;
</#if>
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${entityNameCamel}「值对象」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
<#if properties?has_content && (properties?size > 1)>
@Embeddable
@Getter
@NoArgsConstructor
<#else>
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
</#if>
public class ${entityNameCamel} extends ValueObjectBase {
<#if properties?has_content>
<#list properties as property>
    <#if property.name?contains("Id") || property.name?contains("ID")>

    /**
     * ${property.comment}
     */
    private UIdentifier ${property.name};
    <#else>

    /**
     * ${property.comment}
     */
    private String ${property.name};
    </#if>
</#list>

<#if (properties?size > 1)>

    public ${entityNameCamel}(<#list properties as property><#if property.name?contains("Id") || property.name?contains("ID")>UIdentifier<#else>String</#if> ${property.name}<#if property?has_next>, </#if></#list>) {
<#list properties as property>
        this.${property.name} = ${property.name};
</#list>
    }

    public static class ${entityNameCamel}JsonConverter implements AttributeConverter<${entityNameCamel}, String> {
        @Override
        public String convertToDatabaseColumn(${entityNameCamel} ${entityNameLower}) {
            return JsonUtil.toJson(${entityNameLower});
        }

        @Override
        public ${entityNameCamel} convertToEntityAttribute(String json) {
            return JsonUtil.fromJson(json, ${entityNameCamel}.class);
        }
    }
</#if>
<#else>
    /**
     * ${entityComment}
     */
    private String ${entityNameLower};

</#if>
}
