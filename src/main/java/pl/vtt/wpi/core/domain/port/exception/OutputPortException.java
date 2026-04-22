package pl.vtt.wpi.core.domain.port.exception;

public final class OutputPortException extends PortException {
    public OutputPortException(String message) {
        super(message);
    }

    public OutputPortException(String message, Throwable cause) {
        super(message, cause);
    }
}
