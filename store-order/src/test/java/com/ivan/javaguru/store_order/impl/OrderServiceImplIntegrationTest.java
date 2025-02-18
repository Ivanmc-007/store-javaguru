package com.ivan.javaguru.store_order.impl;

import com.ivan.javaguru.store_order.client.StoreProductCatalogFeignClient;
import com.ivan.javaguru.store_order.config.TopicName;
import com.ivan.javaguru.store_order.usecasses.dto.OrderCreateDto;
import com.ivan.javaguru.store_order.usecasses.dto.OrderResponseDto;
import com.ivan.javaguru.store_order.usecasses.dto.ProductResponseDto;
import com.ivan.javaguru.store_order.usecasses.impl.OrderServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class OrderServiceImplIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockitoBean
    private StoreProductCatalogFeignClient storeProductCatalogFeignClient;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaMessageListenerContainer<String, OrderResponseDto> container;
    private BlockingQueue<ConsumerRecord<String, OrderResponseDto>> records;

    @BeforeAll
    void setUp() {
        ConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(config());
        ContainerProperties containerProperties = new ContainerProperties(TopicName.ORDER_CREATED_EVENT_TOPIC);

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, OrderResponseDto>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    public void testSaveWhenGivenValidOrderThenSuccessfully() throws Exception {
        // Arrange
        Long userId = 10L;
        Long productId = 11L;
        Integer quantity = 2;
        OrderCreateDto createDto = OrderCreateDto.builder()
                .userId(userId).productId(productId).quantity(quantity)
                .build();
        when(storeProductCatalogFeignClient.getProduct(productId)).thenReturn(
                Optional.of(ProductResponseDto.builder().productId(productId).build()));
        // Act
        OrderResponseDto orderResponseDto = orderService.save(createDto);
        // Assert
        assertNotNull(orderResponseDto);
        assertNotNull(orderResponseDto.getOrderId());
        assertEquals(createDto.getUserId(), orderResponseDto.getUserId());
        assertEquals(createDto.getProductId(), orderResponseDto.getProductResponseDto().getProductId());
        assertEquals(createDto.getQuantity(), orderResponseDto.getQuantity());

        ConsumerRecord<String, OrderResponseDto> message = records.poll(3000, TimeUnit.MILLISECONDS);
        assertNotNull(message);
        assertNotNull(message.key());
        OrderResponseDto orderResponseDto1 = message.value();
        assertNotNull(orderResponseDto1);
        assertNotNull(orderResponseDto1.getOrderId());
        assertEquals(createDto.getUserId(), orderResponseDto1.getUserId());
        assertEquals(createDto.getProductId(), orderResponseDto1.getProductResponseDto().getProductId());
        assertEquals(createDto.getQuantity(), orderResponseDto1.getQuantity());
    }

    private Map<String, Object> config() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "test-store-order");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.ivan.javaguru.*");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return config;
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }
}
