package by.yankavets.exception.location;

public class LocationNotFound extends RuntimeException {

    public static final String MESSAGE = "No locations with such name ";

    public LocationNotFound() {
        super(MESSAGE);
    }
}
