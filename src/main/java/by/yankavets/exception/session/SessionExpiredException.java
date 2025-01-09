package by.yankavets.exception.session;

public class SessionExpiredException extends RuntimeException {

    public static final String MESSAGE = "Session expired";

    public SessionExpiredException() {
        super(MESSAGE);
    }
}
