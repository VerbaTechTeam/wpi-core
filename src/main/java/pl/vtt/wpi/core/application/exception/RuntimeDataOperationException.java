package pl.vtt.wpi.core.application.exception;

public class RuntimeDataOperationException extends Exception {
    public RuntimeDataOperationException(String message) {
        super(message);
    }

    public RuntimeDataOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
