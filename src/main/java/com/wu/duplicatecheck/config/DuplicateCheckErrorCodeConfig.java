package com.wu.duplicatecheck.config;

import com.wu.era.library.exception.exceptiontype.WUServiceException;
import com.wu.era.library.exception.utils.WUServiceExceptionUtils;
import com.wu.duplicatecheck.constants.DuplicateCheckConstants;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DuplicateCheckErrorCodeConfig {

    private static final Logger logger = LogManager.getLogger(DuplicateCheckErrorCodeConfig.class);

    private final WUServiceExceptionUtils wuServiceExceptionUtils;

    public WUServiceException prepareErrorCode(Exception exception, String flowName) {
        logger.error("Exception occurred in {}: {}", flowName, exception.getMessage());
        return wuServiceExceptionUtils.buildWUServiceException(
                DuplicateCheckConstants.DUPLICATE_CHECK_ERROR_CODE,
                " Exception in the flow: "+flowName,
        exception
        );
    }
}
