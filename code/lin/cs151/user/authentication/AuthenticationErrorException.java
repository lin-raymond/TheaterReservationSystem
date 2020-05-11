package lin.cs151.user.authentication;

/**
 * Exception which is thrown when an authentication error has occurred
 *
 * @author Raymond Lin
 */
public class AuthenticationErrorException extends Exception {

    /**
     * Creates a new exception when user cannot be authenticated because of an error
     *
     * @param message a user friendly message
     */
    public AuthenticationErrorException(String message) {
        super(message);
    }
}
