package pl.vtt.wpi.core.application.exception;

public class NetworkConfigurationException extends Exception {
    public NetworkConfigurationException(String message) {
        super(message);
    }

    public NetworkConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
