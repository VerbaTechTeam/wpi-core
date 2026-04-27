package pl.vtt.wpi.core.application.exception;

public class NetworkConfigurationException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public NetworkConfigurationException(String message) {
        super(message);
    }

    public NetworkConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
