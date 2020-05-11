package lin.cs151.user.management;

import lin.cs151.user.authentication.Authentication;

/**
 * Contains the users login credentials. The username and the salt is in plain text, but the password is salted and hashed.
 *
 * @author Raymond Lin
 */
public class User implements Comparable<User> {

    private String username;
    private String salt;
    private String hashedPassword;

    /**
     * Create new user account
     * @param username username for new account
     * @param password password for new account
     */
    public User(String username, String password) {
        this.username = username;
        Authentication newUser = new Authentication();
        String[] saltHashedPassword = newUser.generateHashSaltPassword(password);
        this.salt = saltHashedPassword[0];
        this.hashedPassword = saltHashedPassword[1];
    }

    /**
     * Process user from users.txt file
     * @param username username from file
     * @param salt      salt from file
     * @param password salted password from file
     */
    public User(String username, String salt, String password) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = password;
    }

    /**
     * Get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the salt
     *
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Get the hashed password
     *
     * @return the hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Checks if the two User objects are the same
     *
     * @param user the user to compare with
     * @return true if they are the same
     */
    public boolean equals(Object user) {
        User otherUser = (User) user;
        return this.compareTo(otherUser) == 0;
    }

    /**
     * Compares two User objects against each other
     * @param other the other user object to compare against
     * @return an integer saying which user object should come first alphabetically
     */
    public int compareTo(User other) {
        return username.compareTo(other.username);
    }

    /**
     * Outputs the data in the user class into a meaningful string
     * @return a string containing all the data in this User
     */
    public String toString() {
        return username + " " + salt + " " + hashedPassword;
    }
}
