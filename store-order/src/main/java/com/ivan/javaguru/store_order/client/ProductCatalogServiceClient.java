package com.ivan.javaguru.store_order.client;

import com.ivan.javaguru.store_order.config.BeanName;
import com.ivan.javaguru.store_order.usecasses.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCatalogServiceClient {

    @Qualifier(BeanName.PRODUCT_CATALOG_REST_TEMPLATE)
    private final RestTemplate template;

    public Optional<ProductResponseDto> getProduct(Long productId) {
        try {
            ResponseEntity<ProductResponseDto> response = template.getForEntity(
                    "/store-product-catalog/api/v1/product/{productId}", ProductResponseDto.class, productId);
            if (HttpStatus.NO_CONTENT == response.getStatusCode())
                return Optional.empty();
            return Optional.of(response.getBody());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
