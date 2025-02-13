package com.ivan.javaguru.store_order.usecasses.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto implements OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private ProductResponseDto productResponseDto;
    private Integer quantity;
}
