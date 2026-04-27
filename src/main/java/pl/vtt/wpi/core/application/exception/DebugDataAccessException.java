package pl.vtt.wpi.core.application.exception;

public class DebugDataAccessException extends Exception {
    public DebugDataAccessException(String message) {
        super(message);
    }

    public DebugDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
