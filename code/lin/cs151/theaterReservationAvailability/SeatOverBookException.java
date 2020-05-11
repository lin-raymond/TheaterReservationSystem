package lin.cs151.theaterReservationAvailability;

/**
 * Exception which is thrown when a requested seat is already reserved.
 *
 * @author Raymond Lin
 */
public class SeatOverBookException extends Exception {

    /**
     * Creates a new exception when requested seat is reserved already
     *
     * @param message a user friendly message
     */
    public SeatOverBookException(String message) {
        super(message);
    }
}
