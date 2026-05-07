package pl.vtt.wpi.core.application.exception;

public class DeviceRebootException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public DeviceRebootException(String message) {
        super(message);
    }

    public DeviceRebootException(String message, Throwable cause) {
        super(message, cause);
    }
}
