package com.wu.duplicatecheck.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.duplicatecheck.config.KafkaProducerConfig;
import com.wu.duplicatecheck.model.kafka.DuplicateCheckKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicateCheckKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProducerConfig kafkaProducerConfig;
    private final ObjectMapper objectMapper;

    public void publishDuplicateCheckEvent(DuplicateCheckKafkaEvent event) {
        try {
            String eventPayload = objectMapper.writeValueAsString(event);
            String topic = kafkaProducerConfig.getTopic();

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, event.getCorrelationId(), eventPayload);

            kafkaTemplate.send(record);
            log.info("Successfully published DuplicateCheckKafkaEvent to topic {}: {}", topic, eventPayload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize DuplicateCheckKafkaEvent", e);
        } catch (Exception ex) {
            log.error("Failed to publish message to Kafka", ex);
        }
    }
}
