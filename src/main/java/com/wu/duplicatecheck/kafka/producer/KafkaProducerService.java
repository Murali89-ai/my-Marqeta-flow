package com.wu.duplicatecheck.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.duplicatecheck.config.KafkaTopicsConfig;
import com.wu.duplicatecheck.constants.DuplicateCheckConstants;
import com.wu.duplicatecheck.model.kafka.DuplicateCheckKafkaEvent;
import com.wu.duplicatecheck.exception.utils.WUServiceExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;
    private final ObjectMapper objectMapper;

    public void publishEvent(DuplicateCheckKafkaEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                kafkaTopicsConfig.getBusinessTopic(), event.getCorrelationId(), message
            );

            kafkaTemplate.send(record);
            log.info("Successfully published event to Kafka topic [{}] with key [{}]: {}",
                kafkaTopicsConfig.getBusinessTopic(), event.getCorrelationId(), message);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize Kafka event", e);
            throw WUServiceExceptionUtils.buildWUServiceException(
                DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE,
                "Error serializing Kafka event",
                e
            );
        } catch (Exception e) {
            log.error("Failed to publish Kafka event", e);
            throw WUServiceExceptionUtils.buildWUServiceException(
                DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE,
                "Error publishing event to Kafka",
                e
            );
        }
    }

    public void send(String topic, Object message, Headers headers) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, null, null, jsonMessage, headers);
            kafkaTemplate.send(record);
            log.info("Message sent to Kafka topic: {}", topic);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message for Kafka", e);
            throw WUServiceExceptionUtils.buildWUServiceException(
                    DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE,
                    "Error serializing message for Kafka");
        }
    }
}