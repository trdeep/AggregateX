package ${packageName}.${moduleNameLower}.application.service.impl;

import ${packageName}.${moduleNameLower}.application.service.${interfaceName};
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
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
</#if>
import ${packageName}.${moduleNameLower}.domain.service.DomainService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * ${serviceName}「应用服务实现」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Slf4j
@Service("${moduleNameLower}_${implementationName}")
public class ${implementationName} implements ${interfaceName} {

    @Resource
    private DomainService domainService;

    // @Resource
    // private ${entityNameCamel}AggregateRepository ${entityNameLower}Repository;


<#-- 生成自定义服务方法实现 -->
<#if serviceMethods?has_content>
<#list serviceMethods as method>
    @Override
    public ${method.returnType} ${method.name}(<#if method.parameters?has_content><#list method.parameters as param>${param.type} ${param.name}<#if param_has_next>, </#if></#list></#if>) {
        // TODO 实现${method.comment}
        log.info("应用服务：${method.comment}");
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
        <#elseif method.returnType?contains("List<")>
        return List.of();
        <#elseif method.returnType?contains("Optional<")>
        return Optional.empty();
        <#else>
        return null;
        </#if>
</#if>
    }

</#list>
</#if>
}
