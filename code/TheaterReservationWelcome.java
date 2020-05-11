import lin.cs151.fileManagement.ReservationFileManagement;
import lin.cs151.reservationManager.ReservationManager;
import lin.cs151.user.authentication.AuthenticationErrorException;
import lin.cs151.user.management.User;
import lin.cs151.user.management.UserExistsException;
import lin.cs151.user.management.UserManagement;

import java.util.Scanner;

/**
 * The system welcomes users to the reservation system
 *
 * @author Raymond Lin
 */
public class TheaterReservationWelcome {

    private Scanner in;
    private UserManagement userDataBase;

    // pass through after successful sign in
    private ReservationManager reservationManager;

    /**
     * Initializes Theater Reservation Welcome Screen
     *
     * @param in                 the Scanner to import for collecting user input
     * @param userDataBase       the pre initialized user database containing username, salts, and hashed passwords
     * @param reservationManager the pre initialized reservation manager containing all previously registered reservations
     */
    public TheaterReservationWelcome(Scanner in, UserManagement userDataBase, ReservationManager reservationManager) {
        this.in = in;
        this.userDataBase = userDataBase;
        this.reservationManager = reservationManager;
    }

    /**
     * Prompts user to type in option.
     *
     * @return String containing option
     */
    public String selectPrompt() {
        System.out.print("Select Option (U, I, X): ");
        return in.nextLine();
    }

    /**
     * Attempts to login or sign up user based on selection
     *
     * @return User
     * @throws AuthenticationErrorException thrown when user cannot be found during login attempt
     */
    public User login() throws AuthenticationErrorException {
        String[] userData = fetchUsernamePassword();
        String username = userData[0];
        String password = userData[1];
        return userDataBase.signIn(username, password);
    }

    /**
     * Allows new user to make an account on the system
     *
     * @return a boolean value detailing successful sign up
     */
    public boolean signUp() {
        // new user sign up
        System.out.println("New User Registration:");
        String[] userData = fetchUsernamePassword();
        String username = userData[0];
        String password = userData[1];
        try {
            return userDataBase.signUp(username, password) != null;
        } catch (UserExistsException x) {
            System.out.println(x.toString());
        }
        return false;
    }

    /**
     * Captures user's username and password from keyboard
     *
     * @return a String array containing username and password in plain text
     */
    private String[] fetchUsernamePassword() {
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();
        String[] userData = new String[2];
        userData[0] = username;
        userData[1] = password;
        return userData;
    }

    /**
     * Display all available options to user
     */
    private void displayWelcomeMenu() {
        System.out.println("Sign [U]p   Sign [I]n   E[X]it");
    }

    /**
     * Display out all possible options and begins to prompt user for selection. After selection, it will pass through decision tree.
     * Upon successful login, system goes to TheaterReservationMainSystem.
     * This is the main program flowchart designed to not terminate until "E[X]it" has been selected.
     */
    public void initialMenuProgramFlow() {
        System.out.println("If this is your first time using the system, please choose 'U' \"for Sign [U]p\" when prompted.");
        do {
            displayWelcomeMenu();
            String selection = selectPrompt();
            if (selection.compareToIgnoreCase("u") == 0) {
                if (signUp()) {
                    System.out.println("New User successfully registered. Please sign in with newly created credentials.");
                }
            } else if (selection.compareToIgnoreCase("i") == 0) {
                User user = null;
                try {
                    user = login();
                } catch (AuthenticationErrorException x) {
                    System.out.println(x.getMessage());
                }
                if (user != null) {
                    new TheaterReservationMainSystem(user, in, reservationManager);
                }
            } else if (selection.compareToIgnoreCase("x") == 0) {
                ReservationFileManagement reservationFileManagement = new ReservationFileManagement();
                reservationFileManagement.writeReservation(reservationManager);
                break;

            } else {
                System.out.println("Please choose a valid option");
            }
        } while (true);
    }
}
