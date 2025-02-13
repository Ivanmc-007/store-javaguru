package com.ivan.javaguru.store_order.usecasses.dto;

public interface OrderCreatedEvent {
    Long getOrderId();
    Long getUserId();
    ProductResponseDto getProductResponseDto();
    Integer getQuantity();
}
