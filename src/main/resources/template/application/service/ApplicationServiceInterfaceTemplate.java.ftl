package ${packageName}.${moduleNameLower}.application.service;

<#-- 根据方法参数和返回类型添加必要的import -->
<#assign needEntityImport = false>
<#assign importedTypes = []>
<#if serviceMethods?has_content>
<#list serviceMethods as method>
<#if method.returnType?contains(entityNameCamel)>
<#assign needEntityImport = true>
</#if>
<#if method.parameters?has_content>
<#list method.parameters as param>
<#if param.type?contains(entityNameCamel)>
<#assign needEntityImport = true>
</#if>
</#list>
</#if>
</#list>
</#if>

<#if needEntityImport>
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel};
</#if>

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${serviceName}「应用服务接口」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
public interface ${interfaceName} {

<#-- 生成自定义服务方法 -->
<#if serviceMethods?has_content>
<#list serviceMethods as method>
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
    ${method.returnType} ${method.name}(<#if method.parameters?has_content><#list method.parameters as param>${param.type} ${param.name}<#if param_has_next>, </#if></#list></#if>);

</#list>
</#if>
}
