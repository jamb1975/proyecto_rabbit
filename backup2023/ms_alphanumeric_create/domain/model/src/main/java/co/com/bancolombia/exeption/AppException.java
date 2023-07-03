package co.com.bancolombia.exeption;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

public class AppException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppException.class);

    public AppException(String message, GeneralSecurityException e) {
        super(message);
        LOGGER.error(e.getMessage());
    }
    public AppException(String message) {
        super(message);
    }
}
