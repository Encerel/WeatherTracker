package by.yankavets.exception;

public class PassworsdNotMatchException extends RuntimeException {

    public static final String MESSAGE = "Passwords don't match!";

    public PassworsdNotMatchException() {
        super(MESSAGE);
    }
}
