package com.meteo.meteo.Utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogUtil {
    private final Logger logger = Logger.getLogger(LogUtil.class);

    public void log(String info) {
        logger.info(info);
    }

    public void logError(String code, String error) {
        logger.error(String.format("%s: %s", code, error));
    }

    public void logException(Exception exception) {
        logger.error(String.format("Exception: %s, StackTrace: %s", exception.getMessage(), exception.getStackTrace()));
    }

    public void logWarning(String warning) {
        logger.warn(warning);
    }
}
