package pl.vtt.wpi.core.application.exception;

public class AuthenticationServiceUnavailableException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public AuthenticationServiceUnavailableException(String message) {
        super(message);
    }

    public AuthenticationServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
