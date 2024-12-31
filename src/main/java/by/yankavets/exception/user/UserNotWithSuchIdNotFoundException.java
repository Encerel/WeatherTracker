package by.yankavets.exception.user;

public class UserNotWithSuchIdNotFoundException extends RuntimeException {

    public static final String MESSAGE_WITH_ID = "User with id %d not found!";

    public UserNotWithSuchIdNotFoundException(Integer userId) {
        super(String.format(MESSAGE_WITH_ID, userId));
    }
}
