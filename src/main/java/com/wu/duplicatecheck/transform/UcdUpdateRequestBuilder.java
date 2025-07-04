package com.wu.duplicatecheck.transform;

import com.wu.duplicatecheck.dto.ProfileUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UcdUpdateRequestBuilder {

    public Map<String, Object> buildRequest(ProfileUpdateRequest request) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("customerUmn", request.getCustomerUmn());
        payload.put("firstName", request.getFirstName());
        payload.put("lastName", request.getLastName());
        payload.put("email", request.getEmail());
        payload.put("phone", request.getPhone());
        payload.put("address", request.getAddress());

        // Add other fields from ProfileUpdateRequest if required
        // payload.put("fieldName", request.getFieldName());

        log.debug("UCD Payload Built: {}", payload);
        return payload;
    }
}