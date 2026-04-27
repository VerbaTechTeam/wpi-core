package pl.vtt.wpi.core.application.exception;

public class DeviceInfoReadException extends Exception {
    public DeviceInfoReadException(String message) {
        super(message);
    }

    public DeviceInfoReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
