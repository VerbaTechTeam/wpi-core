package pl.vtt.wpi.core.application.exception;

public class PixelProgramServiceException extends Exception {
    public PixelProgramServiceException(String message) {
        super(message);
    }

    public PixelProgramServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
