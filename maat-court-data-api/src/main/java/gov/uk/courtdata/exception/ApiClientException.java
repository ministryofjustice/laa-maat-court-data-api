package gov.uk.courtdata.exception;

/**
 * The type Api client exception.
 * Generic top-level runtime exception used to wrap all api error responses
 */
public class ApiClientException extends RuntimeException {

    /**
     * Instantiates a new Api client exception.
     */
    public ApiClientException() {
        super();
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param message the message
     */
    public ApiClientException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param rootCause the root cause
     */
    public ApiClientException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Instantiates a new Api client exception.
     *
     * @param message   the message
     * @param rootCause the root cause
     */
    public ApiClientException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
