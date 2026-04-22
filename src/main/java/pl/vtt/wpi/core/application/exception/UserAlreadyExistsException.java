package pl.vtt.wpi.core.application.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("User with the given username already exists.");
    }
}
