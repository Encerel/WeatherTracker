package by.yankavets.exception.mapper;

public class UnsupportedMapMethodException extends RuntimeException {

    public static final String MESSAGE = "Method %s is not supported";

    public UnsupportedMapMethodException(String message) {
        super(String.format(MESSAGE, message));
    }
}
