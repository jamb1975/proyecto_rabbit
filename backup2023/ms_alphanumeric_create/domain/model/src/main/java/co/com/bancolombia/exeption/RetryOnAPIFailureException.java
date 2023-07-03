package co.com.bancolombia.exeption;

public class RetryOnAPIFailureException extends RuntimeException {

    public RetryOnAPIFailureException(String message) {
        super(message);
    }

    RetryOnAPIFailureException() {
        super();
    }
}
