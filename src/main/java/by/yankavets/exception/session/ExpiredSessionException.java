package by.yankavets.exception.session;

public class ExpiredSessionException extends RuntimeException {

    public static final String MESSAGE = "Session expired";

    public ExpiredSessionException() {
        super(MESSAGE);
    }
}
