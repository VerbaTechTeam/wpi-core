package pl.vtt.wpi.core.application.exception;

public class DeviceRebootException extends Exception {
    public DeviceRebootException(String message) {
        super(message);
    }

    public DeviceRebootException(String message, Throwable cause) {
        super(message, cause);
    }
}
