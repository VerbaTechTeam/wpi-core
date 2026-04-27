package pl.vtt.wpi.core.application.exception;

public class AuthenticationServiceUnavailableException extends Exception {
    public AuthenticationServiceUnavailableException(String message) {
        super(message);
    }

    public AuthenticationServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
