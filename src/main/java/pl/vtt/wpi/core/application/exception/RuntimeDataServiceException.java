package pl.vtt.wpi.core.application.exception;

public class RuntimeDataServiceException extends Exception {
    public RuntimeDataServiceException(String message) {
        super(message);
    }

    public RuntimeDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
