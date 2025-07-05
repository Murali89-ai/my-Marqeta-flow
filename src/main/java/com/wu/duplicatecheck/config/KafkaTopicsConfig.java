package com.wu.duplicatecheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsConfig {
    private String businessTopic;
    private String errorTopic;
}
