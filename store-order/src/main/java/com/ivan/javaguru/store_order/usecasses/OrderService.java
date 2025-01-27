package com.ivan.javaguru.store_order.usecasses;

import com.ivan.javaguru.store_order.usecasses.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrderService {
    Optional<OrderResponseDto> findById(Long orderId);
}
