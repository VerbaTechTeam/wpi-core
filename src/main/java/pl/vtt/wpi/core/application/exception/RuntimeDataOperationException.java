package pl.vtt.wpi.core.application.exception;

public class RuntimeDataOperationException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public RuntimeDataOperationException(String message) {
        super(message);
    }

    public RuntimeDataOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
