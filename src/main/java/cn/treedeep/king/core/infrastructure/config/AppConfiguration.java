package cn.treedeep.king.core.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 * <p>
 * 负责配置应用级别的组件和属性，包括：
 * <ul>
 * <li>应用属性配置</li>
 * <li>通用组件配置</li>
 * <li>基础设施配置</li>
 * </ul>
 */
@Configuration("king_core_AppConfiguration")
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

}
