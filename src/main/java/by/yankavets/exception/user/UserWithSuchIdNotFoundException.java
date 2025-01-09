package by.yankavets.exception.user;

public class UserWithSuchIdNotFoundException extends RuntimeException {

    private static final String MESSAGE_WITH_ID = "User with id %d not found!";
    private static final String MESSAGE_WITH_NULL_ID = "Invalid user id!";


    public UserWithSuchIdNotFoundException() {
        super(MESSAGE_WITH_NULL_ID);
    }

    public UserWithSuchIdNotFoundException(Integer userId) {
        super(String.format(MESSAGE_WITH_ID, userId));
    }

}
