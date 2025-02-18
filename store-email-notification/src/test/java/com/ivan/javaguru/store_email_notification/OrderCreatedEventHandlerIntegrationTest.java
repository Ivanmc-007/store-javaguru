package com.ivan.javaguru.store_email_notification;

import com.ivan.javaguru.store_email_notification.handler.OrderCreatedEventHandler;
import com.ivan.javaguru.store_email_notification.persistence.model.OrderCreatedEventUnique;
import com.ivan.javaguru.store_email_notification.persistence.repository.OrderCreatedEventUniqueRepo;
import com.ivan.javaguru.store_email_notification.usecasses.dto.OrderCreatedEvent;
import com.ivan.javaguru.store_email_notification.usecasses.dto.ProductResponseDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.ivan.javaguru.store_email_notification.config.TopicName.ORDER_CREATED_EVENT_TOPIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka
@SpringBootTest(properties = {
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class OrderCreatedEventHandlerIntegrationTest {

    @MockitoBean
    private OrderCreatedEventUniqueRepo orderCreatedEventUniqueRepo;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockitoSpyBean
    private OrderCreatedEventHandler orderCreatedEventHandler;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeAll
    void setUp() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, Object> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(config);
        kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Test
    void testHandleWhenReceivedDataThenSuccessfully() throws ExecutionException, InterruptedException {
        // Arrange
        Long orderId = 1L;
        Long userId = 10L;
        Integer quantity = 2;
        Long productId = 11L;
        BigDecimal price = new BigDecimal("100.25");
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(orderId).userId(userId).quantity(quantity)
                .productResponseDto(ProductResponseDto.builder()
                        .productId(productId).price(price)
                        .build())
                .build();
        String messageId = "888";
        String key = String.valueOf(orderId);

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                ORDER_CREATED_EVENT_TOPIC,
                key,
                orderCreatedEvent
        );
        record.headers().add("messageId", messageId.getBytes(StandardCharsets.UTF_8));

        when(orderCreatedEventUniqueRepo.findByMessageId(messageId)).thenReturn(Optional.empty());
        OrderCreatedEventUnique eventUnique = OrderCreatedEventUnique.builder()
                .messageId(messageId)
                .orderId(String.valueOf(orderCreatedEvent.getOrderId()))
                .build();
        when(orderCreatedEventUniqueRepo.save(eventUnique)).thenReturn(null);
        // Act
        kafkaTemplate.send(record).get();
        // Assert
        ArgumentCaptor<String> messageIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonValueCaptor = ArgumentCaptor.forClass(String.class);

        verify(orderCreatedEventHandler, timeout(5000).times(1))
                .handle(messageIdCaptor.capture(), keyCaptor.capture(), jsonValueCaptor.capture());

        assertEquals(messageId, messageIdCaptor.getValue());
        assertEquals(key, keyCaptor.getValue());
    }
}
