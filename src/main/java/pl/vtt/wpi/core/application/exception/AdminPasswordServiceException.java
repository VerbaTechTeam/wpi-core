package pl.vtt.wpi.core.application.exception;

public class AdminPasswordServiceException extends Exception {
    public AdminPasswordServiceException(String message) {
        super(message);
    }

    public AdminPasswordServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
