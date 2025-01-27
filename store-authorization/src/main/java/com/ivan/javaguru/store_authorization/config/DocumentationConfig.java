package com.ivan.javaguru.store_authorization.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name}") String appName,
                                 @Value("${springdoc.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version(appVersion)
                        .description("Open API for " + appName)
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://springdoc.org/")));
    }
}
