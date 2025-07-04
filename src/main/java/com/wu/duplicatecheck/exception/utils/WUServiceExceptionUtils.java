package com.wu.duplicatecheck.exception.utils;

import com.wu.duplicatecheck.exception.exceptiontype.WUServiceException;

public class WUServiceExceptionUtils {

    public static WUServiceException buildWUServiceException(String errorCode, String message, Throwable cause) {
        return new WUServiceException(errorCode, message, cause);
    }

    public static WUServiceException buildWUServiceException(String errorCode, String message) {
        return new WUServiceException(errorCode, message);
    }
}
