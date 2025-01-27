package com.ivan.javaguru.store_product_catalog.usecasses.impl;

import com.ivan.javaguru.store_product_catalog.persistence.repository.ProductRepo;
import com.ivan.javaguru.store_product_catalog.usecasses.ProductService;
import com.ivan.javaguru.store_product_catalog.usecasses.dto.ProductResponseDto;
import com.ivan.javaguru.store_product_catalog.usecasses.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponseDto> findById(Long productId) {
        return productRepo.findById(productId).map(productMapper::toProductResponseDto);
    }
}
