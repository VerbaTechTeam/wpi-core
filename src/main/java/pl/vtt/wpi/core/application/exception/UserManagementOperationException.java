package pl.vtt.wpi.core.application.exception;

public class UserManagementOperationException extends ApplicationServiceException {
    private static final long serialVersionUID = 1L;

    public UserManagementOperationException(String message) {
        super(message);
    }

    public UserManagementOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
