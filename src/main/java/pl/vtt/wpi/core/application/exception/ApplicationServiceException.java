package pl.vtt.wpi.core.application.exception;

public abstract class ApplicationServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    protected ApplicationServiceException(String message) {
        super(message);
    }

    protected ApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
