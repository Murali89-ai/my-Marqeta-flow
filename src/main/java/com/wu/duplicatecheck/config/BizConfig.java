package com.wu.duplicatecheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "biz")
public class BizConfig {
    private String getCardDetailsHost;
    private String getCardDetailsPath;
    private String changePinPath;
    private String serviceId;
    private String userAgent;
    private String ocpApimSubscriptionKey;
}
