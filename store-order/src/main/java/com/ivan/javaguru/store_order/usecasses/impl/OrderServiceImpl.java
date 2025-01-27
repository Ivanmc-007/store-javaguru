package com.ivan.javaguru.store_order.usecasses.impl;

import com.ivan.javaguru.store_order.client.ProductCatalogServiceClient;
import com.ivan.javaguru.store_order.exception.ProductNotFoundException;
import com.ivan.javaguru.store_order.persistence.model.Order;
import com.ivan.javaguru.store_order.persistence.repository.OrderRepo;
import com.ivan.javaguru.store_order.usecasses.OrderService;
import com.ivan.javaguru.store_order.usecasses.dto.OrderResponseDto;
import com.ivan.javaguru.store_order.usecasses.dto.ProductResponseDto;
import com.ivan.javaguru.store_order.usecasses.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final ProductCatalogServiceClient productCatalogServiceClient;

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDto> findById(Long orderId) {
        Optional<Order> o = orderRepo.findById(orderId);
        return o.map(this::getProductFromServiceAndConvertToDto);
    }

    private OrderResponseDto getProductFromServiceAndConvertToDto(Order order) {
        OrderResponseDto orderDto = orderMapper.toOrderResponseDto(order);
        if (order.getProductId() != null) {
            Optional<ProductResponseDto> o = productCatalogServiceClient.getProduct(order.getProductId());
            if (o.isPresent()) {
                orderDto.setProductResponseDto(o.get());
                return orderDto;
            }
            throw new ProductNotFoundException("Product not found (productId: %s) for order (orderId: %s)".formatted(order.getProductId(), order.getOrderId()));
        }
        throw new ProductNotFoundException("Product not found (productId: %s) for order (orderId: %s)".formatted(order.getProductId(), order.getOrderId()));
    }
}
