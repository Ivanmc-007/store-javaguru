package com.ivan.javaguru.store_product_catalog.usecasses;

import com.ivan.javaguru.store_product_catalog.usecasses.dto.ProductResponseDto;

import java.util.Optional;

public interface ProductService {
    Optional<ProductResponseDto> findById(Long productId);
}
