package pl.vtt.wpi.core.application.exception;

public class DataInconsistencyException extends Exception {
    public DataInconsistencyException(String message) {
        super(message);
    }

    public DataInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
