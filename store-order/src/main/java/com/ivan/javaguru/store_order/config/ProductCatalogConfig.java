package com.ivan.javaguru.store_order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ProductCatalogConfig {

    @Bean(name = BeanName.PRODUCT_CATALOG_REST_TEMPLATE)
    @LoadBalanced
    public RestTemplate productCatalogRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://STORE-PRODUCT-CATALOG"));
        return restTemplate;
    }
}
