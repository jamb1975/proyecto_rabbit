package co.com.bancolombia.exeption;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }
}
