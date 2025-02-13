package com.ivan.javaguru.store_order.usecasses.impl;

import com.ivan.javaguru.store_order.client.StoreProductCatalogFeignClient;
import com.ivan.javaguru.store_order.config.BeanName;
import com.ivan.javaguru.store_order.exception.ProductNotFoundException;
import com.ivan.javaguru.store_order.persistence.model.Order;
import com.ivan.javaguru.store_order.persistence.repository.OrderRepo;
import com.ivan.javaguru.store_order.usecasses.OrderService;
import com.ivan.javaguru.store_order.usecasses.dto.OrderCreateDto;
import com.ivan.javaguru.store_order.usecasses.dto.OrderCreatedEvent;
import com.ivan.javaguru.store_order.usecasses.dto.OrderResponseDto;
import com.ivan.javaguru.store_order.usecasses.dto.ProductResponseDto;
import com.ivan.javaguru.store_order.usecasses.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ivan.javaguru.store_order.config.TopicName.ORDER_CREATED_EVENT_TOPIC;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final StoreProductCatalogFeignClient storeProductCatalogFeignClient;

    @Qualifier(BeanName.KAFKA_TEMPLATE_ORDER_CREATED_EVENT)
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplateOrderCreatedEvent;

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDto> findById(Long orderId) {
        Optional<Order> o = orderRepo.findById(orderId);
        return o.map(this::getProductFromServiceAndConvertToDto);
    }

    private OrderResponseDto getProductFromServiceAndConvertToDto(Order order) {
        OrderResponseDto orderDto = orderMapper.toOrderResponseDto(order);
        if (order.getProductId() != null) {
            Optional<ProductResponseDto> o = storeProductCatalogFeignClient.getProduct(order.getProductId());
            if (o.isPresent()) {
                orderDto.setProductResponseDto(o.get());
                return orderDto;
            }
            throw new ProductNotFoundException("Product not found (productId: %s) for order (orderId: %s)".formatted(order.getProductId(), order.getOrderId()));
        }
        throw new ProductNotFoundException("Product not found (productId: %s) for order (orderId: %s)".formatted(order.getProductId(), order.getOrderId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto save(OrderCreateDto dto) {
        Order newOrder = orderMapper.toOrder(dto);
        Optional<ProductResponseDto> o = storeProductCatalogFeignClient.getProduct(dto.getProductId());
        if (o.isPresent()) {
            newOrder = orderRepo.save(newOrder);
            OrderResponseDto responseDto = orderMapper.toOrderResponseDto(newOrder);
            responseDto.setProductResponseDto(o.get());
            kafkaTemplateOrderCreatedEvent.send(ORDER_CREATED_EVENT_TOPIC, String.valueOf(newOrder.getOrderId()), responseDto);
            return responseDto;
        }
        throw new ProductNotFoundException("Trying to add a non-existent product (productId: %s)".formatted(dto.getProductId()));
    }
}