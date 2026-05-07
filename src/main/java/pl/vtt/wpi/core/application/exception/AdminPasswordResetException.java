package pl.vtt.wpi.core.application.exception;

public class AdminPasswordResetException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public AdminPasswordResetException(String message) {
        super(message);
    }

    public AdminPasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }
}
