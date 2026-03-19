package pl.vtt.wpi.core.application.exception;

public class IncorrectUsernameOrPasswordException extends Exception {
    public IncorrectUsernameOrPasswordException() {
        super("Incorrect username or password");
    }
}
