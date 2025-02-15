package com.ivan.javaguru.store_email_notification.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.javaguru.store_email_notification.config.BeanName;
import com.ivan.javaguru.store_email_notification.exception.NonRetryableException;
import com.ivan.javaguru.store_email_notification.persistence.model.OrderCreatedEventUnique;
import com.ivan.javaguru.store_email_notification.persistence.repository.OrderCreatedEventUniqueRepo;
import com.ivan.javaguru.store_email_notification.usecasses.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventHandler {

    private final OrderCreatedEventUniqueRepo orderCreatedEventUniqueRepo;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(
            topics = "order-created-event-topic",
            containerFactory = BeanName.ORDER_CREATED_EVENT_CONTAINER_FACTORY)
    public void handle(@Header("messageId") String messageId,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
                       @Payload String jsonValue) {
        try {
            log.info("Received: "); // just for testing
            log.info("key: " + key); // just for testing
            log.info("value: " + jsonValue); // just for testing
            OrderCreatedEvent event = objectMapper.readValue(jsonValue, OrderCreatedEvent.class);
            Optional<OrderCreatedEventUnique> o = orderCreatedEventUniqueRepo.findByMessageId(messageId);
            if (o.isPresent()) {
                log.info("Duplicate messageId: {}", messageId);
                return;
            }
            // TODO: here should be your logic
            //  (for example get data from another service and send notification to the user by email)
            OrderCreatedEventUnique eventUnique = OrderCreatedEventUnique.builder()
                    .messageId(messageId)
                    .orderId(String.valueOf(event.getOrderId()))
                    .build();
            orderCreatedEventUniqueRepo.save(eventUnique);
        } catch (JsonProcessingException | DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new NonRetryableException(e.getMessage(), e);
        }
    }
}
