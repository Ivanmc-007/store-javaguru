package com.ivan.javaguru.store_gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthorizationConfig {

    @Bean(name = BeanName.AUTHORIZATION_WEB_CLIENT_BUILDER)
    @LoadBalanced
    public WebClient.Builder authorizationWebClientBuilder() {
        return WebClient.builder();
    }
}
