package pl.vtt.wpi.core.application.exception;

public class UserNotExistsException extends Exception {
    public UserNotExistsException() {
        super("User does not exist.");
    }
}
