package by.yankavets.exception;

import jakarta.validation.constraints.Pattern;

public class UserAlreadyExistException extends RuntimeException {

    public static final String MESSAGE_WITH_ID = "User with email %s already exists";

    public UserAlreadyExistException(String email) {
        super(String.format(MESSAGE_WITH_ID, email));
    }
}
