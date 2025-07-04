package com.wu.duplicatecheck.service;

import jakarta.validation.constraints.NotBlank;

public interface MarqetaUpdateService {
    void process(@NotBlank String rawJson);
}
