import lin.cs151.reservationManager.ReservationManager;
import lin.cs151.user.management.UserManagement;

import java.util.Scanner;

/**
 * Reservation system terminal
 *
 * @author Raymond Lin
 */
public class TheaterReservationTerminal {

    private Scanner in;
    private UserManagement userMangement;
    private ReservationManager reservationManager;

    /**
     * Start up initialization of important components
     */
    public void powerUp() {
        this.userMangement = new UserManagement();
        this.reservationManager = new ReservationManager(in);
    }

    /**
     * Get system wide scanner
     * @return Scanner to be used throughout program run time
     */
    public Scanner getScanner() {
        return in;
    }

    /**
     * Get system wide user database
     *
     * @return user database throughout program run time
     */
    public UserManagement getUserManagement() {
        return userMangement;
    }

    /**
     * Gets reservation manager for use throughout system run time
     *
     * @return the reservation manager
     */
    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    /**
     * Creates a new reservation terminal to be used by user throughout system run time
     */
    public TheaterReservationTerminal() {
        this.in = new Scanner(System.in);
    }

    /**
     * Main program
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // read reservation file and initialize user management
        TheaterReservationTerminal terminal = new TheaterReservationTerminal();
        terminal.powerUp();
        TheaterReservationWelcome welcomeScreen = new TheaterReservationWelcome(terminal.getScanner(), terminal.getUserManagement(), terminal.getReservationManager());
        welcomeScreen.initialMenuProgramFlow();
        // close the system wide scanner
        terminal.getScanner().close();
    }
}
