package com.wu.duplicatecheck.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicateCheckEventPublisher {

    private final KafkaTemplate<String ,Object> kafkaTemplate;

    public void send(String topic, Object event, String key, List<Header> headers) {
        try {
            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    topic,
                    null,  // Partition
                    key,
                    event,
                    headers
            );

            kafkaTemplate.send(record);
            log.info("Published event to Kafka for txnId={}", key);

        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }
    }
}