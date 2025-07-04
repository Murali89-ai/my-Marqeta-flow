package com.wu.duplicatecheck.config;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsConfig {
    private String businessTopic;
    private String errorTopic;
}
