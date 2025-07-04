package com.wu.duplicatecheck.adaptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.wu.duplicatecheck.config.MarqetaConfig;
import com.wu.duplicatecheck.constants.DuplicateCheckConstants;
import com.wu.era.library.exception.exceptiontype.WUServiceException;
import com.wu.era.library.exception.utils.WUServiceExceptionUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarqetaAdaptor {

    private final MarqetaConfig config;
    private final RestTemplate restTemplate;
    private final RetryTemplate marqetaRetryTemplate;

    private String baseUrl;

    @PostConstruct
    private void init() {
        this.baseUrl = config.getHost();
    }

    public JsonNode getCardDetails(String cardToken) {
        return marqetaRetryTemplate.execute(context -> {
            try {
                String url = baseUrl + config.getCardGetPath() + cardToken;
                log.info("Calling Marqeta GET Card Details: {}", url);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.AUTHORIZATION, config.getAuthorizationToken());

                HttpEntity<Void> entity = new HttpEntity<>(headers);
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

                return response.getBody();
            } catch (RestClientException e) {
                log.error("Error in getCardDetails", e);
                throw buildServiceException("Failed to fetch card details from Marqeta", e);
            }
        });
    }

    public JsonNode updateCardDetails(String cardToken, JsonNode updatePayload) {
        return marqetaRetryTemplate.execute(context -> {
            try {
                String url = baseUrl + config.getCardPutPath() + cardToken;
                log.info("Calling Marqeta PUT Card Update: {}", url);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.AUTHORIZATION, config.getAuthorizationToken());

                HttpEntity<JsonNode> requestEntity = new HttpEntity<>(updatePayload, headers);
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, JsonNode.class);

                return response.getBody();
            } catch (RestClientException e) {
                log.error("Error in updateCardDetails", e);
                throw buildServiceException("Failed to update card details in Marqeta", e);
            }
        });
    }

    private WUServiceException buildServiceException(String message, Exception e) {
        return WUServiceExceptionUtils.buildWUServiceException(
                DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE,
                message, e
        );
    }
}
