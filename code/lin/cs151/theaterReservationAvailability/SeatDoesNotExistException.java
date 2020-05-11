package lin.cs151.theaterReservationAvailability;

/**
 * Exception which is thrown when a requested seat does not exist.
 *
 * @author Raymond Lin
 */
public class SeatDoesNotExistException extends Exception {

    /**
     * Creates a new exception when seat does not exist
     *
     * @param message a user friendly message
     */
    public SeatDoesNotExistException(String message) {
        super(message);
    }
}
