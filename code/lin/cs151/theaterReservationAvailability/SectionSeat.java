package lin.cs151.theaterReservationAvailability;

import lin.cs151.reservationManager.ReservationManagerMode;
import lin.cs151.reservationData.Reservation;

import java.util.*;

/**
 * Handles seat availability for a corresponding show time
 *
 * @author Raymond Lin
 */
public class SectionSeat {

    private final int MAIN_FLOOR_SECTION_MAX = 50;
    private final int SOUTH_BALCONY_SECTION_MAX = 25;
    private final int EAST_BALCONY_SECTION_MAX = 100;
    private final int WEST_BALCONY_SECTION_MAX = 100;

    private boolean[] mainFloorLeft;
    private boolean[] mainFloorRight;
    private boolean[] mainFloorCenter;
    private boolean[] southBalconyUpper;
    private boolean[] southBalconyLower;
    private boolean[] eastBalcony;
    private boolean[] westBalcony;

    private Map<SeatType, boolean[]> mapSeatType;

    /**
     * Creates a new instance of the seat availability manager
     */
    public SectionSeat() {
        loadSeats();
        loadMap();
    }

    /**
     * Creates storage of all available seats
     */
    private void loadSeats() {
        mainFloorLeft = new boolean[MAIN_FLOOR_SECTION_MAX];
        mainFloorCenter = new boolean[MAIN_FLOOR_SECTION_MAX];
        mainFloorRight = new boolean[MAIN_FLOOR_SECTION_MAX];
        southBalconyLower = new boolean[SOUTH_BALCONY_SECTION_MAX];
        southBalconyUpper = new boolean[SOUTH_BALCONY_SECTION_MAX];
        eastBalcony = new boolean[EAST_BALCONY_SECTION_MAX];
        westBalcony = new boolean[WEST_BALCONY_SECTION_MAX];
    }

    /**
     * Maps out the seat type to the individual storage
     */
    private void loadMap() {
        mapSeatType = new TreeMap<>();
        mapSeatType.put(SeatType.WEST_BALCONY, westBalcony);
        mapSeatType.put(SeatType.EAST_BALCONY, eastBalcony);
        mapSeatType.put(SeatType.SOUTH_BALCONY_UPPER, southBalconyUpper);
        mapSeatType.put(SeatType.SOUTH_BALCONY_LOWER, southBalconyLower);
        mapSeatType.put(SeatType.MAIN_FLOOR_CENTER, mainFloorCenter);
        mapSeatType.put(SeatType.MAIN_FLOOR_RIGHT, mainFloorRight);
        mapSeatType.put(SeatType.MAIN_FLOOR_LEFT, mainFloorLeft);
    }

    /**
     * Changes the availability of a particular seat number
     *
     * @param idNumber the numeric seat number
     * @param seatType the seat type to reserve
     * @param mode     the mode that the {@link lin.cs151.reservationManager.ReservationManager ReservationManager} is in
     * @return if specified seat was successfully reserved (inverted)
     */
    private boolean changeSeatReservationStatus(int idNumber, SeatType seatType, ReservationManagerMode mode) {
        boolean[] seatTypeArray = mapSeatType.get(seatType);
        if (mode == ReservationManagerMode.LOAD || mode == ReservationManagerMode.RESERVE) {
            if (seatTypeArray[idNumber]) {
                return true;
            }
            seatTypeArray[idNumber] = true;
        } else {
            seatTypeArray[idNumber] = false;
        }
        return false;
    }

    /**
     * Attempts to reserve a specified seat
     *
     * @param seatNumber numeric component of seat number
     * @param seatType   the seat type derived from seat number
     * @param mode       the mode that the {@link lin.cs151.reservationManager.ReservationManager ReservationManager} is in
     * @return successful reservation of seat
     * @throws SeatOverBookException if seat was already reserved
     */
    private boolean reserve(int seatNumber, SeatType seatType, ReservationManagerMode mode) throws SeatOverBookException {
        String generalErrorMessage = "We did not attempt to reserve any of the above mentioned seats. Please try again.";
        int seatAdjust = seatNumber - 1;
        switch (seatType) {
            case MAIN_FLOOR_LEFT:
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat m" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case MAIN_FLOOR_RIGHT:
                seatAdjust = seatAdjust - MAIN_FLOOR_SECTION_MAX;
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat m" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case MAIN_FLOOR_CENTER:
                seatAdjust = seatAdjust - (2 * MAIN_FLOOR_SECTION_MAX);
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat m" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case SOUTH_BALCONY_UPPER:
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat sb" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case SOUTH_BALCONY_LOWER:
                seatAdjust = seatAdjust - SOUTH_BALCONY_SECTION_MAX;
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat sb" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case EAST_BALCONY:
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat eb" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
            case WEST_BALCONY:
                if (changeSeatReservationStatus(seatAdjust, seatType, mode)) {
                    throw new SeatOverBookException("Seat wb" + seatNumber + " already reserved." + generalErrorMessage);
                }
                break;
        }
        return true;
    }

    /**
     * Will attempt to reserve a list of seat numbers
     *
     * @param arrayOfSeatID a list of seat numbers
     * @param reservation   the reservation to associate reserved seats with
     * @param mode          the mode that the {@link lin.cs151.reservationManager.ReservationManager ReservationManager} is in
     * @throws SeatOverBookException     if the seat has already been reserved
     * @throws SeatDoesNotExistException if the specified seat number does not exist
     */
    public void reserveSeats(String arrayOfSeatID, Reservation reservation, ReservationManagerMode mode) throws SeatOverBookException, SeatDoesNotExistException {
        String[] seatID = parseStringToStringArray(arrayOfSeatID);
        for (String s : seatID) {
            SeatType seatType = determineSeatType(s);
            int seatNumber = Integer.parseInt(s.replaceAll("[^\\d.]", ""));
            if (mode == ReservationManagerMode.RESERVE) {
                if (reserve(seatNumber, seatType, mode)) {
                    reservation.addSeats(s);
                }
            } else if (mode == ReservationManagerMode.CANCEL) {
                if (reserve(seatNumber, seatType, mode)) {
                    reservation.removeReservedSeat(s);
                }
            } else if (mode == ReservationManagerMode.LOAD) {
                reserve(seatNumber, seatType, mode);
            }
        }
    }

    /**
     * Counts the number of seats types in a particular {@link Reservation reservation} for {@link lin.cs151.ticketCost.ReservationPriceCalculator ReservationPriceCalculator}
     *
     * @param seatCount a mapping of the seat type and the number counted so far
     * @param seatIDs   the list of seat numbers from a {@link Reservation reservation}
     */
    public void determineSeatTypeAndCount(Map<SeatType, Integer> seatCount, List<String> seatIDs) {
        try {
            for (String s : seatIDs) {
                SeatType seatType = determineSeatType(s); // determine seat type
                int currentCount = seatCount.get(seatType); // get count for seat type
                currentCount++; // increment count
                seatCount.put(seatType, currentCount); // put it back into seatCount
            }
        } catch (SeatDoesNotExistException e) {
            // shouldn't happen
        }
    }

    /**
     * Determines a seat type from the seat number
     *
     * @param seatID the seat number to determine a seat type
     * @return a seat type as defined by {@link SeatType}
     * @throws SeatDoesNotExistException if the seat number does not have a defined seat type
     */
    private SeatType determineSeatType(String seatID) throws SeatDoesNotExistException {
        String seatLoc = seatID.replaceAll("[^A-Za-z]", "");
        int seatNumber = Integer.parseInt(seatID.replaceAll("[^\\d.]", ""));
        if (seatLoc.compareToIgnoreCase("m") == 0) {
            if (seatNumber <= 50) {
                return SeatType.MAIN_FLOOR_LEFT;
            } else if (seatNumber <= 100) {
                return SeatType.MAIN_FLOOR_RIGHT;
            } else if (seatNumber <= 150) {
                return SeatType.MAIN_FLOOR_CENTER;
            }
        } else if (seatLoc.compareToIgnoreCase("sb") == 0) {
            if (seatNumber <= 25) {
                return SeatType.SOUTH_BALCONY_UPPER;
            } else if (seatNumber <= 50) {
                return SeatType.SOUTH_BALCONY_LOWER;
            }
        } else if (seatLoc.compareToIgnoreCase("wb") == 0) {
            return SeatType.WEST_BALCONY;
        } else if (seatLoc.compareToIgnoreCase("eb") == 0) {
            return SeatType.EAST_BALCONY;
        }
        throw new SeatDoesNotExistException("One of the seats you have attempted to select does not exist. We did not attempt to add any seats. Please try again.");
    }

    /**
     * Parses a whole string of multiple seat numbers into an array of seat numbers
     *
     * @param stringArray a whole string of multiple seat numbers needed to be parsed
     * @return a list of seat numbers
     */
    private String[] parseStringToStringArray(String stringArray) {
        return stringArray.split(", ");
    }

    /**
     * Compiles all available seat numbers for this seat availability manager
     *
     * @param builder a {@see StringBuilder} to build a String containing required data
     */
    private void printAvailableSeats(StringBuilder builder) {
        builder.append("Main Floor: \n");
        printSeatNumbers(builder, "m", mainFloorLeft, 0);
        printSeatNumbers(builder, "m", mainFloorRight, MAIN_FLOOR_SECTION_MAX);
        printSeatNumbers(builder, "m", mainFloorCenter, 2 * MAIN_FLOOR_SECTION_MAX);
        builder.append("South Balcony: \n");
        printSeatNumbers(builder, "sb", southBalconyUpper, 0);
        printSeatNumbers(builder, "sb", southBalconyLower, SOUTH_BALCONY_SECTION_MAX);
        builder.append("West Balcony: \n");
        printSeatNumbers(builder, "wb", westBalcony, 0);
        builder.append("East Balcony: \n");
        printSeatNumbers(builder, "eb", eastBalcony, 0);
    }

    /**
     * Compiles all unavailable  seat numbers by seat type in this instance of the seat availability manager
     *
     * @param builder  the {@see StringBuilder} to use
     * @param seatType the characters of a seat type to use
     * @param array    a boolean array detailing availability of a particular seat number
     * @param offset   a predefined offset set by this instance of the seat availability manager
     */
    private void printSeatNumbers(StringBuilder builder, String seatType, boolean[] array, int offset) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i]) {
                builder.append(seatType);
                builder.append(i + 1 + offset);
                if (!(i + 1 >= array.length))
                    builder.append(", ");
            }
            if (i % 25 == 0 && i != 0) {
                builder.append("\n");
            }
        }
        builder.append("\n");
    }

    /**
     * Compiles all available seats for this instance of the seat availability manager
     *
     * @return a String containing all available seats
     */
    public String viewAvailableSeats() {
        StringBuilder builder = new StringBuilder();
        builder.append("Available Seats: \n");
        printAvailableSeats(builder);
        return builder.toString();
    }
}
