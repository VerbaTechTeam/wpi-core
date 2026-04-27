package pl.vtt.wpi.core.application.exception;

/**
 * Thrown when debug operations cannot read or clear data due to I/O/port failures.
 */
public class DebugDataAccessException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public DebugDataAccessException(String message) {
        super(message);
    }

    public DebugDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
