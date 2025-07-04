package com.wu.duplicatecheck.model.common.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Kafka metadata passed through services to maintain traceability across events.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String correlationId;
    private String transactionId;
    private String clientId;
    private String applicationName;
    private String userId;
    private String externalRefId;
    private String sourceSystem;
    private String transactionTimestamp;
}
