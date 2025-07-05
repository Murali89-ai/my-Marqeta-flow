package com.wu.duplicatecheck.exception.utils;

import com.wu.duplicatecheck.exception.exceptiontype.WUExceptionType;
import com.wu.duplicatecheck.exception.exceptiontype.WUServiceException;
import org.springframework.stereotype.Component;

@Component
public class WUServiceExceptionUtils {

    public static WUServiceException buildWUServiceException(WUExceptionType type, String message, Throwable cause) {
        return new WUServiceException(type, message, cause);
    }

    public static WUServiceException buildWUServiceException(WUExceptionType type, String message) {
        return new WUServiceException(type, message);
    }

    public static WUServiceException buildWUServiceException(WUExceptionType type) {
        return new WUServiceException(type);
    }

    public static WUServiceException buildWUServiceException(String errorCode, String message, Throwable type) {
        return new WUServiceException(errorCode,message,type);
    }
}
