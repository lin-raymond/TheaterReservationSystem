package lin.cs151.user.management;

/**
 * Exception which is thrown when a {@link User user} with a specific {@link User#getUsername() username} cannot be found.
 */
public class UserNotFoundException extends Exception {

    /**
     * Creates an exception when a specific {@link User#getUsername() username} could not be found in the system
     *
     * @param message a user friendly message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
