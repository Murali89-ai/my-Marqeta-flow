package com.wu.duplicatecheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "marqeta")
public class MarqetaConfig {

    private String host;
    private String authenticatePath;
    private String cardGetPath;
    private String cardPutPath;
    private String cardTokenExpiryPath;

    private int retryCount;
    private int retryDelay;

    private String authorizationUsername;
    private String authorizationPassword;
    private String authorizationToken; // Can be loaded via Secrets Manager in actual env
}
