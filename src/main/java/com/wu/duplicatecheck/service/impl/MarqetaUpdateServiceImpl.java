package com.wu.duplicatecheck.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.duplicatecheck.adaptor.MarqetaAdaptor;
import com.wu.duplicatecheck.config.KafkaTopicsConfig;
import com.wu.duplicatecheck.constants.DuplicateCheckConstants;
import com.wu.duplicatecheck.kafka.KafkaHeadersUtil;
import com.wu.duplicatecheck.kafka.producer.KafkaProducerService;
import com.wu.duplicatecheck.model.kafka.DuplicateCheckKafkaEvent;
import com.wu.duplicatecheck.service.MarqetaUpdateService;
import com.wu.duplicatecheck.validation.RequestValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarqetaUpdateServiceImpl implements MarqetaUpdateService {

    private final MarqetaAdaptor          marqetaAdaptor;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaTopicsConfig       kafkaTopics;
    private final RequestValidator validator;
    private final ObjectMapper            mapper;

    /**
     * Consume the DLQ‐message (or a direct call) and invoke Marqeta.
     *
     * @param rawJson the JSON message received (contains cardToken, payload, etc.).
     */
    @Override
    public void process(@NotBlank String rawJson) {

        String correlationId = UUID.randomUUID().toString();
        String externalRefId = "N/A";   // default – will be overridden if present in the message

        try {
            /* ----------------------------------------------------------------
             * 1. Parse & validate the JSON coming from DLQ / upstream caller
             * ---------------------------------------------------------------- */
            JsonNode root         = mapper.readTree(rawJson);
            String   cardToken    = root.path("cardToken").asText();
            JsonNode updateBody   = root.path("updatePayload");
            externalRefId         = root.path("externalRefId").asText(null);

            validator.notBlank(cardToken, "cardToken must not be blank");
            validator.notNull(updateBody, "updatePayload is mandatory");

            /* ----------------------------------------------------------------
             * 2. Call Marqeta adapter
             * ---------------------------------------------------------------- */
            JsonNode marqetaResponse = marqetaAdaptor.updateCardDetails(cardToken, updateBody);
            log.info("Marqeta update successful for cardToken={} → {}", cardToken, marqetaResponse);

            /* ----------------------------------------------------------------
             * 3. Build business-event & push to Kafka
             * ---------------------------------------------------------------- */
            DuplicateCheckKafkaEvent event = DuplicateCheckKafkaEvent.builder()
                    .correlationId(correlationId)
                    .externalRefId(externalRefId)
                    .status(DuplicateCheckConstants.DUPLICATE_CHECK_SUCCESS_CODE)
                    .message("Marqeta card updated")
                    .sourceSystem("MARQETA")
                    .eventType("BUSINESS_EVENT")
                    .eventTime(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .build();

            Headers stdHeaders = KafkaHeadersUtil.buildStandardHeaders(
                    correlationId,
                    externalRefId,
                    "marqeta-update-svc"
            );

            kafkaProducerService.send(
                    kafkaTopics.getBusinessTopic(),
                    mapper.writeValueAsString(event),
                    stdHeaders
            );

        } catch (Exception ex) {

            log.error("Marqeta update failed – publishing error event", ex);

            DuplicateCheckKafkaEvent errorEvent = DuplicateCheckKafkaEvent.builder()
                    .correlationId(correlationId)
                    .externalRefId(externalRefId)
                    .status(DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE)
                    .message(ex.getMessage())
                    .sourceSystem("MARQETA")
                    .eventType("ERROR_EVENT")
                    .eventTime(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .build();

            try {
                Headers stdHeaders = KafkaHeadersUtil.buildStandardHeaders(
                        correlationId,
                        externalRefId,
                        "marqeta-update-svc"
                );

                kafkaProducerService.send(
                        kafkaTopics.getErrorTopic(),
                        mapper.writeValueAsString(errorEvent),
                        stdHeaders
                );
            } catch (Exception e2) {
                log.error("Secondary failure – unable to push Marqeta error event to Kafka", e2);
            }

            /* re-throw for DLQ 2-nd level or controller, as required */
            throw new RuntimeException("Marqeta update failed", ex);
        }
    }

    public static Headers buildStandardHeaders(String correlationId, String externalRefId, String sourceSystem) {
        Headers headers = new RecordHeaders();
        headers.add("correlationId", correlationId.getBytes(StandardCharsets.UTF_8));
        headers.add("externalReferenceId", externalRefId.getBytes(StandardCharsets.UTF_8));
        headers.add("sourceSystem", sourceSystem.getBytes(StandardCharsets.UTF_8));
        return headers;
    }
}
