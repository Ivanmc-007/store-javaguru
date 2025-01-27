package com.ivan.javaguru.store_order.usecasses.mapper;

import com.ivan.javaguru.store_order.persistence.model.Order;
import com.ivan.javaguru.store_order.usecasses.dto.OrderResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {
    public abstract OrderResponseDto toOrderResponseDto(Order entity);
}
