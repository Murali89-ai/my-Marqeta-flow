package com.wu.duplicatecheck.service.impl;

import com.wu.duplicatecheck.config.KafkaTopicsConfig;
import com.wu.duplicatecheck.dto.ProfileUpdateRequest;
import com.wu.duplicatecheck.dto.DuplicateCheckResponse;
import com.wu.duplicatecheck.kafka.KafkaHeadersUtil;
import com.wu.duplicatecheck.kafka.producer.KafkaProducerService;
import com.wu.duplicatecheck.service.DuplicateCheckService;
import com.wu.duplicatecheck.service.HttpService;
import com.wu.duplicatecheck.validation.RequestValidator;
import com.wu.duplicatecheck.transform.UcdUpdateRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Concrete implementation that:
 * <ol>
 *   <li>Validates the inbound {@link ProfileUpdateRequest}</li>
 *   <li>Transforms it to the request format expected by the UCD profile-update API</li>
 *   <li>Sends the transformed payload to the UCD service (synchronous REST)</li>
 *   <li>Publishes a “business-event” message to Kafka for downstream async processing</li>
 * </ol>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DuplicateCheckServiceImpl implements DuplicateCheckService {

    private final RequestValidator      validator;
    private final UcdUpdateRequestBuilder                      ucdRequestBuilder;
    private final HttpService                                  httpService;
    private final KafkaProducerService                         kafkaProducer;
    private final KafkaTopicsConfig                            kafkaTopics;

    @Override
    public DuplicateCheckResponse processProfileUpdate(ProfileUpdateRequest request) {

        /* ---------- 1. Validate incoming request ---------- */
        validator.validate(request);
        log.debug("Request validation SUCCESS for customerUmn={}", request.getCustomerUmn());

        /* ---------- 2. Build UCD-specific request ---------- */
        Map<String, Object> ucdPayload = ucdRequestBuilder.buildRequest(request);
        log.debug("UCD request built -> {}", ucdPayload);

        /* ---------- 3. Invoke UCD profile-update service ---------- */
        Map<String, Object> ucdResponse = httpService.postForObject(
                "/ucd/customer/profile/addorupdate",   // path is externalised in `application.yaml`
                ucdPayload,Map.class);
        log.info("UCD profile-update API responded with {}", ucdResponse);

        /* ---------- 4. Produce Kafka business-event ---------- */
        String correlationId       = UUID.randomUUID().toString();
        String externalReferenceId = request.getCustomerUmn();
        String upstreamServiceName = "duplicate-check-svc";

        Headers headers = KafkaHeadersUtil.buildStandardHeaders(
                correlationId,
                externalReferenceId,
                upstreamServiceName);

        // Serialise as JSON via KafkaProducerService – it hides ObjectMapper details
        kafkaProducer.send(
                kafkaTopics.getBusinessTopic(),
                ucdResponse,
                headers);

        /* ---------- 5. Prepare & return API response ---------- */
        return DuplicateCheckResponse.builder()
                .customerUmn(request.getCustomerUmn())
                .transactionId(correlationId)
                .status("SUCCESS")
                .description("Duplicate-check processed and UCD profile updated")
                .build();
    }
}
