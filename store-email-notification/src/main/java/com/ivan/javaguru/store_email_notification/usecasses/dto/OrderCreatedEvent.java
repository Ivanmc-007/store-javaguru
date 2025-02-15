package com.ivan.javaguru.store_email_notification.usecasses.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private ProductResponseDto productResponseDto;
    private Integer quantity;
}
