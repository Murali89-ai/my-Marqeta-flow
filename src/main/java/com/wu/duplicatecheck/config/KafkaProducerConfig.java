package com.wu.duplicatecheck.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.duplicatecheck.model.kafka.DuplicateCheckKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProducerProperties props;
    private final ObjectMapper objectMapper;

    @Bean
    public ProducerFactory<String, DuplicateCheckKafkaEvent> producerFactory() {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
        cfg.put(ProducerConfig.CLIENT_ID_CONFIG,          props.getClientId());
        cfg.put(ProducerConfig.ACKS_CONFIG,               props.getAcks());
        cfg.put(ProducerConfig.RETRIES_CONFIG,            props.getRetries());
        cfg.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,       props.getMaxBlockTimeInMs());
        cfg.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                props.getMaxInFlightRequestsPerCon());
        cfg.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, props.getRequestTimeoutInMs());
        cfg.put(ProducerConfig.LINGER_MS_CONFIG,          props.getLingerMs());
        cfg.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,props.getDeliveryTimeoutInMs());

        return new DefaultKafkaProducerFactory<>(cfg,
                new StringSerializer(),
                new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, DuplicateCheckKafkaEvent> kafkaTemplate(
            ProducerFactory<String, DuplicateCheckKafkaEvent> pf) {
        return new KafkaTemplate<>(pf);
    }
}
