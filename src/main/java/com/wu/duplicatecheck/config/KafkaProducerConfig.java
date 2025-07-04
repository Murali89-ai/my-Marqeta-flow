package com.wu.duplicatecheck.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.duplicatecheck.model.kafka.DuplicateCheckKafkaEvent;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Builder
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerConfig {

    private String bootStrapServers;
    private String clientId;
    private String acks;
    private Integer retries;
    private Integer maxBlockTimeInMs;
    private Integer maxInFlightRequestsPerCon;
    private Integer requestTimeoutInMs;
    private Integer lingerMs;
    private Integer deliveryTimeoutInMs;
    private String topic;

    @Bean
    public ProducerFactory<String, DuplicateCheckKafkaEvent> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockTimeInMs);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerCon);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutInMs);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutInMs);

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, DuplicateCheckKafkaEvent> kafkaTemplate(ProducerFactory<String, DuplicateCheckKafkaEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // Setters for @ConfigurationProperties
    public void setBootStrapServers(String bootStrapServers) {
        this.bootStrapServers = bootStrapServers;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public void setMaxBlockTimeInMs(Integer maxBlockTimeInMs) {
        this.maxBlockTimeInMs = maxBlockTimeInMs;
    }

    public void setMaxInFlightRequestsPerCon(Integer maxInFlightRequestsPerCon) {
        this.maxInFlightRequestsPerCon = maxInFlightRequestsPerCon;
    }

    public void setRequestTimeoutInMs(Integer requestTimeoutInMs) {
        this.requestTimeoutInMs = requestTimeoutInMs;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

    public void setDeliveryTimeoutInMs(Integer deliveryTimeoutInMs) {
        this.deliveryTimeoutInMs = deliveryTimeoutInMs;
    }
}
