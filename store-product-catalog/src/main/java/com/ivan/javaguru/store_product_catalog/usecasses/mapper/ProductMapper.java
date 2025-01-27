package com.ivan.javaguru.store_product_catalog.usecasses.mapper;

import com.ivan.javaguru.store_product_catalog.persistence.model.Product;
import com.ivan.javaguru.store_product_catalog.usecasses.dto.ProductResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class ProductMapper {
    public abstract ProductResponseDto toProductResponseDto(Product entity);
}
