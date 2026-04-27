package pl.vtt.wpi.core.application.exception;

public class DebugServiceException extends Exception {
    public DebugServiceException(String message) {
        super(message);
    }

    public DebugServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
