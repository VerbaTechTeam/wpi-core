package pl.vtt.wpi.core.application.exception;

public class DebugDataAccessException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public DebugDataAccessException(String message) {
        super(message);
    }

    public DebugDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
