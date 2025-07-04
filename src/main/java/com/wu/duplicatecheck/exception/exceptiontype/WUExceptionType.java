package com.wu.duplicatecheck.exception.exceptiontype;

import lombok.Getter;

@Getter
public enum WUExceptionType {
    
    REQUEST_VALIDATION_EXCEPTION("E1001", "Request validation failed"),
    DATA_NOT_FOUND("E1002", "Requested data not found"),
    UNAUTHORIZED("E1003", "Unauthorized access"),
    INTERNAL_SERVER_ERROR("E1004", "Internal server error"),
    EXTERNAL_SERVICE_FAILURE("E1005", "External service failure"),
    DUPLICATE_RECORD("E1006", "Duplicate record detected");

    private final String code;
    private final String message;

    WUExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}