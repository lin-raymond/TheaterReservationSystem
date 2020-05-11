package lin.cs151.user.management;

/**
 * Exception which is thrown when a username already exists in the system
 *
 * @author Raymond Lin
 */
public class UserExistsException extends Exception {

    /**
     * Creates a new exception if a username is not unique
     *
     * @param message a user friendly message
     */
    public UserExistsException(String message) {
        super(message);
    }
}
