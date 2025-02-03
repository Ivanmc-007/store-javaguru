package com.ivan.javaguru.store_order.client;

import com.ivan.javaguru.store_order.exception.StoreProductCatalogIsNotAvailableException;
import com.ivan.javaguru.store_order.usecasses.dto.ProductResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "STORE-PRODUCT-CATALOG")
public interface StoreProductCatalogFeignClient {

    @GetMapping("/store-product-catalog/api/v1/product/{productId}")
    @Retry(name = "store-product-catalog", fallbackMethod = "getProductInfoFallback")
    @CircuitBreaker(name = "store-product-catalog", fallbackMethod = "getProductInfoFallback")
    Optional<ProductResponseDto> getProduct(@PathVariable Long productId);

    default Optional<ProductResponseDto> getProductInfoFallback(Long productId, Throwable cause) {
        throw new StoreProductCatalogIsNotAvailableException(cause.getMessage(), cause);
    }
}
