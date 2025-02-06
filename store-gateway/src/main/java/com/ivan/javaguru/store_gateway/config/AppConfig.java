package com.ivan.javaguru.store_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean(name = BeanName.REST_TEMPLATE)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
