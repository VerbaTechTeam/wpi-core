package pl.vtt.wpi.core.domain.exception;

public final class InputPortException extends PortException {
    public InputPortException(String message) {
        super(message);
    }

    public InputPortException(String message, Throwable cause) {
        super(message, cause);
    }
}
