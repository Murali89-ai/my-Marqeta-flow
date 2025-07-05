package com.wu.duplicatecheck.transform;

import com.wu.duplicatecheck.model.common.kafka.TransactionData;
import com.wu.duplicatecheck.model.event.DuplicateCheckBusinessEvent;
import org.springframework.stereotype.Component;

@Component
public class ErrorEventNotificationBuilder {

    public DuplicateCheckBusinessEvent buildErrorEvent(TransactionData transactionData, String eventName, String eventType, String eventSource) {
        return DuplicateCheckBusinessEvent.builder()
                .eventName(eventName)
                .eventType(eventType)
                .eventSource(eventSource)
                .payload(transactionData)
                .build();
    }
}
