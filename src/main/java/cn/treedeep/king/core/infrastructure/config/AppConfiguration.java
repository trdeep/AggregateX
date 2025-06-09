package cn.treedeep.king.core.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("king_core_AppConfiguration")
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

}
