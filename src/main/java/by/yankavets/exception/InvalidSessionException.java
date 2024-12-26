package by.yankavets.exception;

public class InvalidSessionException extends RuntimeException {

    private static final String MESSAGE = "Invalid session";

    public InvalidSessionException() {
        super(MESSAGE);
    }
}
