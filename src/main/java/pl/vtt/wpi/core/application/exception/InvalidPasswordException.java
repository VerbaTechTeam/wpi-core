package pl.vtt.wpi.core.application.exception;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException() {
        super("Required password does not meet the criteria.");
    }
}
