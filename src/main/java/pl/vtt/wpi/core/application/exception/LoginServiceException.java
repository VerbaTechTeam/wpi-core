package pl.vtt.wpi.core.application.exception;

public class LoginServiceException extends Exception {
    public LoginServiceException(String message) {
        super(message);
    }

    public LoginServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
