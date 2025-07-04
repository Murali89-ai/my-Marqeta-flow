package com.wu.duplicatecheck.model.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wu.duplicatecheck.model.common.kafka.TransactionData;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuplicateCheckErrorEvent {

    private String errorCode;
    private String errorMessage;
    private String failurePoint;
    private Instant eventTimestamp;
    private TransactionData transactionData;
}
