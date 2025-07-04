package com.wu.duplicatecheck.exception.exceptiontype;

public class WUServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String errorMessage;
    private final Throwable rootCause;

    public WUServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.rootCause = null;
    }

    public WUServiceException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.rootCause = cause;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public Throwable getRootCause() {
        return rootCause != null ? rootCause : this.getCause();
    }
}
