package pl.vtt.wpi.core.application.exception;

public class PixelProgramOperationException extends Exception {
    public PixelProgramOperationException(String message) {
        super(message);
    }

    public PixelProgramOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
