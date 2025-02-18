package com.ivan.javaguru.store_order;

import com.ivan.javaguru.store_order.config.BeanName;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IdempotentProducerIntegrationTest {

    @Autowired
    @Qualifier(BeanName.KAFKA_TEMPLATE)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    @Test
    void testProducerConfigWhenIdempotenceEnabledThenSuccessfully() {
        // Arrange
        ProducerFactory<String, Object> producerFactory = kafkaTemplate.getProducerFactory();
        // Act
        Map<String, Object> config = producerFactory.getConfigurationProperties();
        // Assert
        assertEquals("true", config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG));
        assertEquals("all", config.get(ProducerConfig.ACKS_CONFIG));
    }
}
