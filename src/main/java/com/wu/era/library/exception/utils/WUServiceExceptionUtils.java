package com.wu.era.library.exception.utils;

import com.wu.era.library.exception.exceptiontype.WUServiceException;

public class WUServiceExceptionUtils {

    public static WUServiceException buildWUServiceException(String errorCode, String message, Throwable cause) {
        return new WUServiceException(errorCode, message, cause);
    }

    public static WUServiceException buildWUServiceException(String errorCode, String message) {
        return new WUServiceException(errorCode, message);
    }
}
