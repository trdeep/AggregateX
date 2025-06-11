package cn.treedeep.king.core.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("king_core_RestConfig")
public class RestConfig {

    @Bean("king_core_RestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
