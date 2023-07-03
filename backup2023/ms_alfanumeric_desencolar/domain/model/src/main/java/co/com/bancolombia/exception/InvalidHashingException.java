package co.com.bancolombia.exception;

public class InvalidHashingException extends RuntimeException {

    public InvalidHashingException(final String message) {
        super(message);
    }

    public InvalidHashingException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
