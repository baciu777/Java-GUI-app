package domain.validation;

/**
 * Class that extends RunTimeException
 * is for validation purposes
 */
public class ValidationException extends RuntimeException {
    /**
     * constructor
     */
    public ValidationException() {
    }

    /**
     * constructor
     * @param message string
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * method with message and cause
     * @param message string
     * @param cause throwable
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * method with only cause as parameter
     * @param cause throwable
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * method
     * @param message string
     * @param cause throwable
     * @param enableSuppression boolean
     * @param writableStackTrace boolean
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
