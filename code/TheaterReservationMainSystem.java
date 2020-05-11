import lin.cs151.reservationManager.ReservationManager;
import lin.cs151.user.management.User;

import java.util.Scanner;

/**
 * The main reservation system after successful login by user
 *
 * @author Raymond Lin
 */
public class TheaterReservationMainSystem {

    private User currentUser;
    private ReservationManager reservationManager;
    private Scanner in;

    /**
     * Creates a new instance of the main reservation system
     *
     * @param user               the {@link User user} that is signed into the system
     * @param in                 the system wide scanner in use
     * @param reservationManager the reservation manager to use
     */
    public TheaterReservationMainSystem(User user, Scanner in, ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
        currentUser = user;
        this.in = in;
        System.out.println("Logged in as: " + user.getUsername());
        mainMenuProgramFlow();
    }

    /**
     * Prompts user to type in option.
     *
     * @return String containing option
     */
    public String selectPrompt() {
        System.out.print("Select Option (R, V, C, O): ");
        return in.nextLine();
    }

    /**
     * Details the main program flow which repeats until user selects exit option
     */
    public void mainMenuProgramFlow() {
        do {
            displayMainMenu();
            String selection = selectPrompt();
            if (selection.compareToIgnoreCase("r") == 0) {
                reservationManager.createNewReservation(currentUser);
            } else if (selection.compareToIgnoreCase("v") == 0) {
                String allReservations = reservationManager.viewReservations(currentUser);
                System.out.println(allReservations);
            } else if (selection.compareToIgnoreCase("c") == 0) {
                reservationManager.cancelReservation(currentUser);
            } else if (selection.compareToIgnoreCase("o") == 0) {
                reservationManager.exit(currentUser);
                break;
            } else {
                System.out.println("Please choose a valid option");

            }
        } while (true);
    }

    /**
     * Displays main menu to user
     */
    private void displayMainMenu() {
        System.out.println("[R]eserve    [V]iew    [C]ancel    [O]ut");
    }

}
