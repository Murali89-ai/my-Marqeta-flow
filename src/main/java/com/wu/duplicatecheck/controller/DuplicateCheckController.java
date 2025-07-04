package com.wu.duplicatecheck.controller;

import com.wu.duplicatecheck.dto.ProfileUpdateRequest;
import com.wu.duplicatecheck.dto.DuplicateCheckResponse;
import com.wu.duplicatecheck.service.DuplicateCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v2/dupcheck")
@RequiredArgsConstructor
public class DuplicateCheckController {

    private final DuplicateCheckService duplicateCheckService;

    @PostMapping
    public ResponseEntity<DuplicateCheckResponse> checkDuplicate(
            @Valid @RequestBody ProfileUpdateRequest request) {
        
        log.info("Received duplicate check request for customerUmn: {}", request.getCustomerUmn());
        
        DuplicateCheckResponse response = duplicateCheckService.processProfileUpdate(request);
        return ResponseEntity.ok(response);
    }
}
