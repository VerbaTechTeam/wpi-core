package pl.vtt.wpi.core.domain.exception;

public class SendingException extends RuntimeException {
    public SendingException(String message) {
        super(message);
    }

    public SendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
