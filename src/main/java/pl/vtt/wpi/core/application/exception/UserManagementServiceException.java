package pl.vtt.wpi.core.application.exception;

public class UserManagementServiceException extends Exception {
    public UserManagementServiceException(String message) {
        super(message);
    }

    public UserManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
