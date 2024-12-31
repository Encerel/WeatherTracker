package by.yankavets.exception.user;

public class PasswordsNotMatchException extends RuntimeException {

    public static final String MESSAGE = "Passwords don't match!";

    public PasswordsNotMatchException() {
        super(MESSAGE);
    }
}
