package lin.cs151.user.management;

import lin.cs151.fileManagement.UserFileManagement;
import lin.cs151.user.authentication.Authentication;
import lin.cs151.user.authentication.AuthenticationErrorException;

import java.util.List;
import java.util.TreeSet;

/**
 * Manages all users in the system along with new sign ups.
 *
 * @author Raymond Lin
 */
public class UserManagement {

    private TreeSet<User> users;
    private UserFileManagement userFileManagement;

    /**
     * Creates a new UserManagement instance to hold all users for quick search.
     * Upon system start, UserManagement will query {@link UserFileManagement UserFileManagement} for all preexisting users
     */
    public UserManagement() {
        this.users = new TreeSet<>();
        userFileManagement = new UserFileManagement();
        List<User> users = userFileManagement.userFetch();
        if(users != null) {
            this.users.addAll(users);
        }
    }

    /**
     * Allows user to sign up for account in the system
     * @param username the username for the new account, must be unique, case sensitive
     * @param password the password for the new account, case sensitive
     * @return a new user object ready to write into file and immediately stored in memory
     * @throws UserExistsException if the username already exists in the system, this exception will be thrown
     */
    public User signUp(String username, String password) throws UserExistsException {
        try {
            User user = fetchUser(username);
            String message = "The user: " + username + " already exists. Please log in normally. If you have forgotten your password, please navigate to \\hw1\\data\\users.txt, and delete the line with your own username on it.";
            throw new UserExistsException(message);
        } catch (UserNotFoundException e) {
            // ignored it is normal for user not to be found
        }
        User newUser = new User(username, password);
        userFileManagement.addNewUser(newUser);
        this.users.add(newUser);
        return newUser;
    }

    /**
     * Used by sign up to check for presence of username and by sign in to pull user object containing required salt and hash
     * @param username the username to find
     * @return the user object with that username with salt and hash
     * @throws UserNotFoundException thrown when user object with username cannot be found
     */
    private User fetchUser(String username) throws UserNotFoundException {
        for (User isUser : users) {
            if (isUser.getUsername().compareTo(username) == 0) {
                return isUser;
            }
        }
        throw new UserNotFoundException("I could not find the user: " + username);
    }

    /**
     * Allows users to sign in into the system with a preexisting username and password
     *
     * @param username the username to use to sign in
     * @param password the password to use to sign in
     * @return the {@link User user} object if sign in was successful
     * @throws AuthenticationErrorException if the system could not authenticate the user
     */
    public User signIn(String username, String password) throws AuthenticationErrorException {
        try {
            User targetUser = fetchUser(username);
            if (authenticateUser(password, targetUser)) {
                return targetUser;
            }
        } catch (UserNotFoundException x) {
            throw new AuthenticationErrorException("Incorrect username or password.");
        }
        return null; // statement should not be reachable
    }

    /**
     * Will prepare to authenticate the user using the plain text password against the corresponding {@link User user} object
     *
     * @param password the plain text password to pass through to user authentication
     * @param user     the {@link User user }object containing the {@link User#getUsername() username} to authenticate
     * @return a boolean stating whether or not the user was successfully authenticated
     * @throws AuthenticationErrorException if the system could not authenticate the user
     */
    private boolean authenticateUser(String password, User user) throws AuthenticationErrorException {
        Authentication authentication = new Authentication();
        return authentication.authenticateUser(password, user);
    }
}
