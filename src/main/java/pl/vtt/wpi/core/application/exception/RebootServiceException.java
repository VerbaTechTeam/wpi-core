package pl.vtt.wpi.core.application.exception;

public class RebootServiceException extends Exception {
    public RebootServiceException(String message) {
        super(message);
    }

    public RebootServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
