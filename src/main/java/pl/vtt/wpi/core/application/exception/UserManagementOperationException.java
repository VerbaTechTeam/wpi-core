package pl.vtt.wpi.core.application.exception;

public class UserManagementOperationException extends Exception {
    public UserManagementOperationException(String message) {
        super(message);
    }

    public UserManagementOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
