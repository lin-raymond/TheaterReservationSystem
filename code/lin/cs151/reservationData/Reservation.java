package lin.cs151.reservationData;

import lin.cs151.fileManagement.ReservationFileManagement;
import lin.cs151.user.management.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Handles the structure of each Reservation in memory
 *
 * @author Raymond Lin
 */
public class Reservation implements Comparable<Reservation> {

    private String confirmationNumber;
    private String username;
    private int totalSeatsReserved;
    private LocalDateTime movieTimeSlot;
    private List<String> seatIDs;
    private String tempConfirmationNumber;

    /**
     * Creates a new reservation instance
     *
     * @param username         the username from {@link User#getUsername()}
     * @param selectedTimeSlot the date and time, see {@linkplain LocalDateTime}, to use for this reservation
     */
    public Reservation(String username, LocalDateTime selectedTimeSlot) {
        this.username = username;
        this.confirmationNumber = "";
        this.totalSeatsReserved = 0;
        this.movieTimeSlot = selectedTimeSlot;
        this.seatIDs = new ArrayList<>();
        this.tempConfirmationNumber = "";
    }

    /**
     * Creates a new reservation instance from data retrieved from file by {@link ReservationFileManagement#readReservationFile()}
     *
     * @param confirmationNumber the confirmation number from file
     * @param username           the username from file
     * @param totalSeatsReserved the number of seats reserved from file
     * @param movieTimeSlot      the date and time this reservation is for from file
     * @param seatIDs            a list of seat numbers from file
     */
    public Reservation(String confirmationNumber, String username, int totalSeatsReserved, LocalDateTime movieTimeSlot, List<String> seatIDs) {
        this.confirmationNumber = confirmationNumber;
        this.username = username;
        this.totalSeatsReserved = totalSeatsReserved;
        this.movieTimeSlot = movieTimeSlot;
        this.seatIDs = seatIDs;
        this.tempConfirmationNumber = "";
    }

    /**
     * Sets a temporary confirmation number
     *
     * @param temp a temporary confirmation number
     */
    public void setTempConfirmationNumber(String temp) {
        this.tempConfirmationNumber = temp;
    }

    /**
     * Adds a seat number to this reservation
     *
     * @param seatID seat number to add
     */
    public void addSeats(String seatID) {
        seatIDs.add(seatID);
        addSeatsReserved(1);
    }

    /**
     * Adds a permanent confirmation number
     *
     * @param confirmationNumber permanent confirmation number to add
     */
    public void confirmReservation(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    /**
     * Gets the current confirmation number for this reservation
     *
     * @return the current confirmation number
     */
    public String getConfirmationNumber() {
        if (confirmationNumber.isBlank()) {
            return "Reservation not Confirmed";
        }
        return confirmationNumber;
    }

    /**
     * Checks if a seat number is present
     *
     * @param seatID seat number to check for
     * @return true if seat number is in this reservation, otherwise no
     */
    public boolean checkSeatIDPresent(String seatID) {
        return seatIDs.contains(seatID);
    }

    /**
     * Gets the date and time this reservation is for
     *
     * @return the date and time for this reservation
     */
    public LocalDateTime getMovieTimeSlot() {
        return movieTimeSlot;
    }

    /**
     * Gets the username for this reservation
     *
     * @return the username recorded for this reservation
     */
    public String getUsername() {
        return username;
    }

    /**
     * Removes a seat number from this reservation
     *
     * @param seatID the seat number to remove from this reservation
     */
    public void removeReservedSeat(String seatID) {
        seatIDs.remove(seatID);
        addSeatsReserved(-1);
    }

    /**
     * Get a list of seat numbers as a list
     *
     * @return a list of seat numbers
     */
    public List<String> getSeatIDsAsArray() {
        return seatIDs;
    }

    /**
     * Get a list of seat numbers as a String
     *
     * @return a list of seat numbers
     */
    public String getSeatIDs() {
        StringBuilder builder = new StringBuilder();
        for (String s : seatIDs) {
            builder.append(s);
            builder.append(", ");
        }
        return builder.toString();
    }

    /**
     * Get the number of seats reserved in this reservation
     *
     * @return the number of seats reserved
     */
    public int getTotalSeatsReserved() {
        return totalSeatsReserved;
    }

    /**
     * Add the number of seats reserved
     *
     * @param seatsReserved number of seats just reserved
     */
    private void addSeatsReserved(int seatsReserved) {
        totalSeatsReserved += seatsReserved;
    }

    /**
     * Converts the data stored in this reservation to a String
     *
     * @return data in this reservation as a String
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return "Confirmation Code: \n" +
                getConfirmationNumber() +
                "\n" +
                username +
                "\n" +
                movieTimeSlot.format(formatter) +
                "\n" +
                "Number of seats reserved: \n" +
                totalSeatsReserved +
                "\n" +
                "Seats Reserved: \n" +
                getSeatIDs() +
                "\n";
    }

    /**
     * Compares this reservation against other reservations by date, confirmation number, and temporary confirmation number
     *
     * @param other other reservation to compare against
     * @return a number detailing which reservation should come first
     */
    public int compareTo(Reservation other) {
        int compare = this.movieTimeSlot.compareTo(other.movieTimeSlot);
        // if there are reservations with the same movie date and time, compare with confirmation number
        if (compare == 0) {
            compare = this.confirmationNumber.compareTo(other.confirmationNumber);
            // if there are reservations with the same confirmation number, compare against the temporary confirmation number
            if (compare == 0) {
                compare = this.tempConfirmationNumber.compareTo(other.tempConfirmationNumber);
            }
        }
        return compare;
    }

    /**
     * Checks if two reservations are the same
     *
     * @param o the other reservation to check against this one
     * @return true, if they are the same, otherwise false
     */
    public boolean equals(Object o) {
        Reservation other = (Reservation) o;
        return movieTimeSlot.compareTo(other.movieTimeSlot) == 0;
    }
}
