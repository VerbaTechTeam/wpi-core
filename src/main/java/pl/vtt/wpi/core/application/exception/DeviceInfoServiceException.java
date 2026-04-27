package pl.vtt.wpi.core.application.exception;

public class DeviceInfoServiceException extends Exception {
    public DeviceInfoServiceException(String message) {
        super(message);
    }

    public DeviceInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
