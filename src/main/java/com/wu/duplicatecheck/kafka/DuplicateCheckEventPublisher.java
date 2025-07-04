package com.wu.duplicatecheck.kafka;

import com.wu.duplicatecheck.config.KafkaProducerConfig;
import com.wu.duplicatecheck.model.common.kafka.TransactionData;
import com.wu.duplicatecheck.model.event.DuplicateCheckBusinessEvent;
import com.wu.duplicatecheck.model.event.DuplicateCheckErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicateCheckEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerConfig kafkaConfig;
    private final ObjectMapper objectMapper;

    private List<RecordHeader> defaultHeaders;

    @PostConstruct
    public void initHeaders() {
        this.defaultHeaders = List.of(
            new RecordHeader("source", kafkaConfig.getClientId().getBytes(StandardCharsets.UTF_8)),
            new RecordHeader("topic", kafkaConfig.getTopic().getBytes(StandardCharsets.UTF_8))
        );
    }

    public void publishBusinessEvent(DuplicateCheckBusinessEvent event, TransactionData txnData) {
        try {
            String key = txnData.getTransactionId();
            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    kafkaConfig.getTopic(),
                    null,
                    key,
                    event,
                    defaultHeaders
            );
            kafkaTemplate.send(record);
            log.info("Published business event to Kafka for txnId={}", key);
        } catch (Exception e) {
            log.error("Failed to publish business event", e);
        }
    }

    public void publishErrorEvent(DuplicateCheckErrorEvent errorEvent, TransactionData txnData) {
        try {
            String key = txnData.getTransactionId();
            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    kafkaConfig.getErrorTopic(),
                    null,
                    key,
                    errorEvent,
                    defaultHeaders
            );
            kafkaTemplate.send(record);
            log.info("Published error event to Kafka for txnId={}", key);
        } catch (Exception e) {
            log.error("Failed to publish error event", e);
        }
    }
}
