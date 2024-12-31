package by.yankavets.exception.user;

public class InvalidEmailOrPasswordException extends RuntimeException {

    public static final String MESSAGE = "Invalid email or password";

    public InvalidEmailOrPasswordException() {
        super(MESSAGE);
    }
}
