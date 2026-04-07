package pl.vtt.wpi.core.domain.exception;

public sealed class PortException extends Exception
        permits InputPortException, OutputPortException {
    public PortException(String message) {
        super(message);
    }

    public PortException(String message, Throwable cause) {
        super(message, cause);
    }
}
