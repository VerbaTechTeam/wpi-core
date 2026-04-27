package pl.vtt.wpi.core.application.exception;

public class AdminPasswordResetException extends Exception {
    public AdminPasswordResetException(String message) {
        super(message);
    }

    public AdminPasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }
}
