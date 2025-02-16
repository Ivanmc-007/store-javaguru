package com.ivan.javaguru.store_order.config;

import com.ivan.javaguru.store_order.usecasses.dto.OrderCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.ivan.javaguru.store_order.config.TopicName.ORDER_CREATED_EVENT_TOPIC;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String producerBootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    private String producerAcks;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private String producerEnableIdempotence;

    Map<String, Object> kafkaProducerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, producerAcks);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerEnableIdempotence);
        return config;
    }

    @Bean(name = BeanName.KAFKA_TEMPLATE_ORDER_CREATED_EVENT)
    public KafkaTemplate<String, OrderCreatedEvent> kafkaTemplateOrderCreatedEvent() {
        ProducerFactory<String, OrderCreatedEvent> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic orderCreatedEventTopic() {
        return TopicBuilder.name(ORDER_CREATED_EVENT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
