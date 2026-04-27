package pl.vtt.wpi.core.application.exception;

public class DeviceInfoReadException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public DeviceInfoReadException(String message) {
        super(message);
    }

    public DeviceInfoReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
