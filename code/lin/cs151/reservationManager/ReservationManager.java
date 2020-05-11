package lin.cs151.reservationManager;

import lin.cs151.reservationData.Reservation;
import lin.cs151.fileManagement.ReservationFileManagement;
import lin.cs151.theaterReservationAvailability.MovieTimeSlot;
import lin.cs151.theaterReservationAvailability.SeatDoesNotExistException;
import lin.cs151.theaterReservationAvailability.SeatOverBookException;
import lin.cs151.ticketCost.ReservationPriceCalculator;
import lin.cs151.reservationData.ReservationTotalCostBreakDown;
import lin.cs151.user.management.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Scanner;

/**
 * Manages all reservations while system is running in memory
 *
 * @author Raymond Lin
 */
public class ReservationManager {

    Scanner in;

    private MovieTimeSlot movieTimeSlot;
    private List<Reservation> reservationsInSession;

    private List<Reservation> reservationsMadePreviously;
    private int numberOfReservationsModified;

    /**
     * Creates new instance of the Reservation manaager
     *
     * @param in the Scanner to use
     */
    public ReservationManager(Scanner in) {
        this.in = in;
        // load movie time slots
        movieTimeSlot = new MovieTimeSlot();
        reservationsInSession = new ArrayList<>();
        reservationsMadePreviously = new ArrayList<>();
        numberOfReservationsModified = 0;
        loadFileReservations();
    }

    /**
     * Loads reservations from {@link ReservationFileManagement#readReservationFile()} to memory
     */
    private void loadFileReservations() {
        ReservationFileManagement readReservation = new ReservationFileManagement();
        reservationsMadePreviously.addAll(readReservation.readReservationFile());
        loadPreviouslyReservedReservations();
    }

    /**
     * Updates seat availability dictated by {@link lin.cs151.theaterReservationAvailability.SectionSeat SectionSeat} and {@link MovieTimeSlot} using data from file
     */
    public void loadPreviouslyReservedReservations() {
        for (Reservation r : reservationsMadePreviously) {
            movieTimeSlot.updateWithFile(r);
        }
    }

    /**
     * Creates a new {@link Reservation reservation}
     *
     * @param user             {@link User user} to associate reservation with
     * @param selectedTimeSlot {@link MovieTimeSlot} to associate reservation with
     */
    public void createReservation(User user, LocalDateTime selectedTimeSlot) {
        Reservation newReservation = new Reservation(user.getUsername(), selectedTimeSlot);
        getSeatsToReserveForNewReservation(newReservation);
        if (newReservation.getTotalSeatsReserved() != 0) {
            reservationsInSession.add(newReservation);
        }
    }

    /**
     * Compiles all {@link Reservation reservations} in reservation manager as a list
     *
     * @return a list of {@link Reservation reservations} in reservation manager
     */
    public List<Reservation> compileAllReservation() {
        List<Reservation> compileAllReservations = new ArrayList<>();
        if (!reservationsInSession.isEmpty()) {
            compileAllReservations.addAll(reservationsInSession);
        }
        if (!reservationsMadePreviously.isEmpty()) {
            compileAllReservations.addAll(reservationsMadePreviously);
        }
        return compileAllReservations;
    }

    /**
     * Compiles all {@link Reservation reservations} from file by a {@link User user}
     *
     * @param user {@link User user} to search for reservations for
     * @return a list of {@link Reservation reservations} created by {@link User user} from file
     */
    private List<Reservation> compilePreviousUserReservations(User user) {
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation x : reservationsMadePreviously) {
            if (user.getUsername().compareTo(x.getUsername()) == 0) {
                userReservations.add(x);
            }
        }
        return userReservations;
    }

    /**
     * Compiles all {@link Reservation reservations} created by {@link User user}
     *
     * @param user {@link User user} to search for reservations for
     * @return a list of all {@link Reservation reservations} created by {@link User user}
     */
    public Set<Reservation> compileUserReservation(User user) {
        List<Reservation> compileAllReservations = compileAllReservation();
        Set<Reservation> userReservations = new TreeSet<>();
        int number = 0;
        for (Reservation x : compileAllReservations) {
            if (user.getUsername().compareTo(x.getUsername()) == 0) {
                if (x.getConfirmationNumber().compareTo("Reservation not Confirmed") == 0) {
                    // if unlikely event that two unconfirmed reservations have same date, generate a temporary confirmation number
                    // to make it unique
                    x.setTempConfirmationNumber("temp-" + number);
                }
                userReservations.add(x);
            }
            number++;
        }
        return userReservations;
    }

    /**
     * Compile all newly created {@link Reservation reservations} by {@link User user}
     *
     * @param user {@link User user} to search all newly created reservations by
     * @return a list of all newly created {@link Reservation reservations} by {@link User user}
     */
    private List<Reservation> compileUserInSessionReservation(User user) {
        List<Reservation> userInSessionReservations = new ArrayList<>();
        for (Reservation r : reservationsInSession) {
            if (user.getUsername().compareTo(r.getUsername()) == 0) {
                userInSessionReservations.add(r);
            }
        }
        return userInSessionReservations;
    }

    /**
     * View all {@link Reservation reservations} created by {@link User user}
     *
     * @param user {@link User user} to view {@link Reservation reservations} for
     * @return a list of {@link Reservation reservations} sorted by {@link MovieTimeSlot}
     */
    public String viewReservations(User user) {
        Set<Reservation> userReservation = compileUserReservation(user);
        StringBuilder builder = new StringBuilder();
        String username = user.getUsername();
        builder.append("Reservations for user: ");
        builder.append(username);
        builder.append(": \n");
        // view all reservations in order by show time and confirmation number
        for (Reservation x : userReservation) {
            builder.append(x.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Lists all {@link MovieTimeSlot} associated with a specific set of {@link Reservation reservations}
     *
     * @param userReservation a list of {@link Reservation reservations}
     * @return a list of all {@link MovieTimeSlot} in this set of {@link Reservation reservations}
     */
    private String listUserReservationTimeSlot(Set<Reservation> userReservation) {
        int i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        StringBuilder builder = new StringBuilder();
        for (Reservation r : userReservation) {
            builder.append("(");
            builder.append(i);
            builder.append(")");
            builder.append(" ");
            builder.append(r.getMovieTimeSlot().format(formatter)).append("\n");
            i++;
        }
        return builder.toString();
    }

    /**
     * Get a specific {@link Reservation reservation} from a set of {@link Reservation reservations}
     *
     * @param reservations set of {@link Reservation reservations}
     * @param id           an integer of the corresponding {@link Reservation reservation}
     * @return the {@link Reservation reservation} requested
     */
    private Reservation getReservation(Set<Reservation> reservations, int id) {
        Object[] reservationsArray = reservations.toArray();
        Object selectedReservation = reservationsArray[id - 1];
        return (Reservation) selectedReservation;
    }

    /**
     * Cancel a reservation from {@link User user}
     *
     * @param user {@link User user} cancelling a {@link Reservation reservation}
     */
    public void cancelReservation(User user) {
        System.out.println("Modifying/Cancelling Reservation");
        Set<Reservation> userReservation = compileUserReservation(user);
        System.out.println(listUserReservationTimeSlot(userReservation));
        System.out.println("Type \"done\" to exit.");
        System.out.print("Select show date\\time to modify: ");
        int selection;
        if (!in.hasNextInt()) {
            String select = in.nextLine();
            // exit cancel mode
        } else {
            selection = in.nextInt();
            in.nextLine(); // sanitize input
            Reservation modifyReservation = getReservation(userReservation, selection);
            do {
                if (modifyReservation.getSeatIDsAsArray().isEmpty()) {
                    // if all seats canceled, get out of cancel mode
                    System.out.println("All seats cancelled. Exiting cancel mode.");
                    break;
                }
                // if not all seats were canceled allow user to cancel more
                System.out.println(viewSeatsReserved(modifyReservation));
                System.out.print("Selection: ");
                String select = in.nextLine();
                if (select.compareToIgnoreCase("done") == 0) {
                    // if user says there done, exit cancel mode
                    break;
                } else {
                    String[] seatsSelected = select.split(", ");
                    String seatsValidated = validateCancelSeatSelection(seatsSelected, modifyReservation);
                    reserveSeats(modifyReservation, seatsValidated, ReservationManagerMode.CANCEL);
                }
            } while (true);
            relocateReservationToBeModified(modifyReservation);
        }
    }

    /**
     * Moves a previously made reservation to another List of {@link Reservation reservations} for newly created reservations
     *
     * @param reservation {@link Reservation reservation} to move
     */
    private void relocateReservationToBeModified(Reservation reservation) {
        if (reservationsMadePreviously.contains(reservation) && !reservationsInSession.contains(reservation)) {
            reservationsMadePreviously.remove(reservation);
            reservationsInSession.add(reservation);
            numberOfReservationsModified++;
        }
    }

    /**
     * Checks if seats being removed exist in {@link Reservation reservation}
     *
     * @param seatsToCancel     a list of seat numbers requested to be cancelled
     * @param modifyReservation the reservation to check against
     * @return a String of seats numbers authorized to be removed
     */
    private String validateCancelSeatSelection(String[] seatsToCancel, Reservation modifyReservation) {
        ArrayList<String> seatsValidatedToCancel = new ArrayList<>();
        for (String s : seatsToCancel) {
            if (modifyReservation.checkSeatIDPresent(s)) {
                seatsValidatedToCancel.add(s);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String s : seatsValidatedToCancel) {
            builder.append(s).append(", ");
        }
        return builder.toString();
    }

    /**
     * Gets all seat numbers reserved in {@link Reservation reservation} with user prompt
     *
     * @param reservation {@link Reservation reservation} that is being modified
     * @return a String containing seat numbers held by {@link Reservation reservation} and user prompt
     */
    private String viewSeatsReserved(Reservation reservation) {
        return "Seats Reserved in this Reservation: \n" +
                reservation.getSeatIDs() + "\n" +
                "Please choose seats to remove. \n" +
                "For Example: eb66, eb67, eb68, eb69, eb70, eb71, eb72 \n" +
                "To Not Cancel Seats, Type \"Done\".";
    }

    /**
     * Gets the receipt of confirmed {@link Reservation reservations} for a specific {@link User user}
     *
     * @param user {@link User user} to confirm reservations for
     * @return a String containing information, including price breakdown held by {@link ReservationTotalCostBreakDown}, about all {@link Reservation reservations} created by a {@link User user}
     */
    private String viewReservationReceipt(User user) {
        List<Reservation> userInSessionReservations = compileUserInSessionReservation(user);
        StringBuilder builder = new StringBuilder();
        String reservationSeparator = "---------------------------------------\n";
        // prints out all the newly confirmed reservations
        for (Reservation r : userInSessionReservations) {
            generateConfirmationCode(r, user);
            builder.append(reservationSeparator);
            builder.append("Receipt for Reservation: \n");
            builder.append(r.toString());
            ReservationPriceCalculator calculator = new ReservationPriceCalculator(r);
            ReservationTotalCostBreakDown breakDown = calculator.calculateTotalPrice();
            builder.append(breakDown).append("\n");
            builder.append(reservationSeparator);
        }
        return builder.toString();
    }

    /**
     * Logs out {@link User user} from system
     *
     * @param user {@link User user} to log off
     */
    public void exit(User user) {
        System.out.println(viewReservationReceipt(user));
        System.out.println("Signing out user: " + user.getUsername());
    }

    /**
     * Prompts user to choose new time slot managed by {@link MovieTimeSlot}
     *
     * @return a {@link LocalDateTime} that {@link User user} has chosen
     */
    private LocalDateTime chooseNewTimeSlot() {
        System.out.println("Please Choose Movie Time Slot by using the number listed on the left: ");
        System.out.println(movieTimeSlot.listAvailableTimeSlots());
        System.out.println("If you are done, type in \"Done\".");
        System.out.print("Selection: ");
        int selection;
        if (in.hasNextInt()) {
            selection = in.nextInt();
        } else {
            in.nextLine();
            return null;
        }
        in.nextLine(); // sanitize input buffer
        return movieTimeSlot.getMovieTimeSlot(selection);
    }

    /**
     * Allows an unconfirmed {@link Reservation reservation} to be confirmed and generates new confirmation code for that {@link Reservation reservation}
     *
     * @param newReservation {@link Reservation reservation} to be confirmed
     * @param user           {@link User user} confirming reservation
     */
    private void generateConfirmationCode(Reservation newReservation, User user) {
        if (newReservation.getConfirmationNumber().compareTo("Reservation not Confirmed") == 0) {
            List<Reservation> temp = compilePreviousUserReservations(user);
            int size = temp.size() + numberOfReservationsModified + 1;
            newReservation.confirmReservation(user.getUsername() + "-" + size);
            reservationsMadePreviously.add(newReservation);
            reservationsInSession.remove(newReservation);
        }
    }

    /**
     * Creates a new {@link Reservation reservation} for {@link User user}
     *
     * @param user {@link User user} to create new {@link Reservation reservation} for
     */
    public void createNewReservation(User user) {
        do {
            System.out.println("Beginning New Reservation: ");
            // begin prompting for time slot
            LocalDateTime selectedMovieTimeSlot = chooseNewTimeSlot();
            if (selectedMovieTimeSlot == null) {
                // if user does not select show time, have system cancel reservation process
                System.out.println("Cancelled Reservation Process. Exiting now.");
                break;
            }
            createReservation(user, selectedMovieTimeSlot);
            System.out.println("Reservation process completed. Would you like to make another one? (yes/no)");
            String select = in.nextLine();
            // repeat reservation process if user selects yes, otherwise exits
            if (select.compareToIgnoreCase("yes") != 0) {
                break;
            }
        } while (true);
    }

    /**
     * Starts process to reserve certain seats
     *
     * @param reservation {@link Reservation reservation} reserved seats will be associated with
     * @param seats       a String containing the seat numbers to be reserved
     * @param mode        determines how seats reservation status be updated in {@link lin.cs151.theaterReservationAvailability.SectionSeat SectionSeat}
     */
    private void reserveSeats(Reservation reservation, String seats, ReservationManagerMode mode) {
        try {
            movieTimeSlot.reserveSeats(reservation.getMovieTimeSlot(), seats, reservation, mode);
        } catch (SeatDoesNotExistException | SeatOverBookException e) {
            // if an error occurs, have user press a key to continue on
            System.out.println("Error occurred: \n");
            System.out.println(e.toString());
            System.out.println("Press any key to continue reservation.");
            in.nextLine();
        }
    }

    /**
     * Prompts user for seats to reserve for a particular time slot defined by {@link MovieTimeSlot}
     *
     * @param reservation the {@link Reservation reservation} being to have newly reserved seats associated with
     */
    private void getSeatsToReserveForNewReservation(Reservation reservation) {
        // show seat availability
        // prompt for seat id
        do {
            // user is prompted to pick seats until user says there done
            System.out.println(movieTimeSlot.viewAvailableSeats(reservation.getMovieTimeSlot()));
            System.out.println("Pick your seats. Type seats with commas and spaces exactly as shown. ");
            System.out.println("For Example: eb66, eb67, eb68, eb69, eb70, eb71, eb72");
            System.out.println("To Exit Seat Selection, Type \"Done\".");
            System.out.print("Seat Selection: ");
            String input = in.nextLine();
            if (input.compareToIgnoreCase("done") == 0) {
                // user is done reserving seats, exit loop
                break;
            } else {
                reserveSeats(reservation, input, ReservationManagerMode.RESERVE);
            }
        } while (true);
    }
}
