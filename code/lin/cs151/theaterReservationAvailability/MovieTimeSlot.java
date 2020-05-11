package lin.cs151.theaterReservationAvailability;

import lin.cs151.reservationData.Reservation;
import lin.cs151.reservationManager.ReservationManagerMode;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages all show times for the theater.
 *
 * @author Raymond Lin
 */
public class MovieTimeSlot {

    private Map<LocalDateTime, SectionSeat> timeSlots;

    /**
     * Creates a new instance of show times
     */
    public MovieTimeSlot() {
        timeSlots = new TreeMap<>();
        loadTimeSlots();
    }

    /**
     * Begins to update availability of seats for each show time using {@link Reservation reservation} data.
     * Only occurs once during startup.
     *
     * @param r the {@link Reservation reservation} data to load into memory
     */
    public void updateWithFile(Reservation r) {
        LocalDateTime dateTime = r.getMovieTimeSlot();
        String seatIDs = r.getSeatIDs();
        try {
            if (!seatIDs.isEmpty()) {
                reserveSeats(dateTime, seatIDs, r, ReservationManagerMode.LOAD);
            }
        } catch (SeatDoesNotExistException | SeatOverBookException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get a given show time using unique integer
     *
     * @param number numbered show time to get
     * @return a time slot formatted as {@linkplain LocalDateTime}
     */
    public LocalDateTime getMovieTimeSlot(int number) {
        Set<LocalDateTime> dayAndTime = timeSlots.keySet();
        Object[] daysAndTimes = dayAndTime.toArray();
        Object temp = daysAndTimes[number - 1];
        return (LocalDateTime) temp;
    }

    /**
     * Prints out all the available show times
     *
     * @return a String containing all available show times
     */
    public String listAvailableTimeSlots() {
        StringBuilder builder = new StringBuilder();
        Set<LocalDateTime> dayAndTime = timeSlots.keySet();
        int i = 1;
        for (LocalDateTime x : dayAndTime) {
            builder.append("(");
            builder.append(i);
            builder.append(")");
            builder.append(" ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
            builder.append(x.format(formatter)).append("\n");
            i++;
        }
        return builder.toString();
    }

    /**
     * Shows all available seats for this particular show time. Seat availability managed by {@link SectionSeat}.
     *
     * @param movieTimeSlot show time to use
     * @return a String containing all available seats
     */
    public String viewAvailableSeats(LocalDateTime movieTimeSlot) {
        SectionSeat availableSeats = timeSlots.get(movieTimeSlot);
        return availableSeats.viewAvailableSeats();
    }

    /**
     * Begins to reserve seats for a particular show time
     *
     * @param dateTime    show time to reserve seats for
     * @param seatId      a String containing all seats numbers to reserve
     * @param reservation the reservation to add the reserved seats to
     * @param mode        the mode in which the reservation system is in. {@link ReservationManagerMode}
     * @throws SeatDoesNotExistException thrown when a requested seat does not exist
     * @throws SeatOverBookException     thrown when a requested seat is already reserved
     */
    public void reserveSeats(LocalDateTime dateTime, String seatId, Reservation reservation, ReservationManagerMode mode) throws SeatDoesNotExistException, SeatOverBookException {
        SectionSeat movieTimeSlotSeatAvailability = timeSlots.get(dateTime);
        movieTimeSlotSeatAvailability.reserveSeats(seatId, reservation, mode);

    }

    /**
     * Loads all the show times
     */
    private void loadTimeSlots() {
        LocalDate daySlot = LocalDate.of(2020, 12, 23);
        LocalTime timeSlot1 = LocalTime.of(18, 30);
        LocalTime timeSlot2 = LocalTime.of(20, 30);
        while (daySlot.isBefore(LocalDate.of(2021, 1, 3))) {
            LocalDateTime movieTimeSlot = LocalDateTime.of(daySlot, timeSlot1);
            timeSlots.put(movieTimeSlot, new SectionSeat());
            movieTimeSlot = LocalDateTime.of(daySlot, timeSlot2);
            timeSlots.put(movieTimeSlot, new SectionSeat());
            daySlot = daySlot.plusDays(1);
        }
    }
}
