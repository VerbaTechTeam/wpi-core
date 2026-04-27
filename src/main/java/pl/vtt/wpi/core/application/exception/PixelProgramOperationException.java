package pl.vtt.wpi.core.application.exception;

public class PixelProgramOperationException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public PixelProgramOperationException(String message) {
        super(message);
    }

    public PixelProgramOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
