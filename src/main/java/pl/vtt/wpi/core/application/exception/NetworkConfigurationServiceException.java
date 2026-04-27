package pl.vtt.wpi.core.application.exception;

public class NetworkConfigurationServiceException extends Exception {
    public NetworkConfigurationServiceException(String message) {
        super(message);
    }

    public NetworkConfigurationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
