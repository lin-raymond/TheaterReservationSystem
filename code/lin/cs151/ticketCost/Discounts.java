package lin.cs151.ticketCost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Holds all discount eligibility data
 *
 * @author Raymond Lin
 */
public class Discounts {

    private double discountNight;
    private ArrayList<LocalDateTime> eligibleDiscountNight;
    private int[] groupDiscountSeatMinimum;
    private double[] groupDiscount;

    /**
     * Creates a new instance of the discount eligibility class
     */
    public Discounts() {
        loadDiscounts();
    }

    /**
     * Loads all discount eligibility data and discount values
     */
    private void loadDiscounts() {
        discountNight = 20;
        eligibleDiscountNight = new ArrayList<>();
        eligibleDiscountNight.add(LocalDateTime.of(LocalDate.of(2020, 12, 26), LocalTime.of(18, 30)));
        eligibleDiscountNight.add(LocalDateTime.of(LocalDate.of(2020, 12, 26), LocalTime.of(20, 30)));
        eligibleDiscountNight.add(LocalDateTime.of(LocalDate.of(2020, 12, 27), LocalTime.of(18, 30)));
        eligibleDiscountNight.add(LocalDateTime.of(LocalDate.of(2020, 12, 27), LocalTime.of(20, 30)));
        groupDiscount = new double[]{2, 5};
        groupDiscountSeatMinimum = new int[]{11, 5};
    }

    /**
     * Gets eligible discounts for particular show time and by number of seats
     *
     * @param movieTimeSlot the show time to use for discount eligibility
     * @param numberOfSeats the number of seats reserved by a {@link lin.cs151.reservationData.Reservation reservation}
     * @return the discount applied
     */
    public double getDiscounts(LocalDateTime movieTimeSlot, int numberOfSeats) {
        if (isDiscountNight(movieTimeSlot)) {
            return getDiscountNightPrice();
        } else {
            if (numberOfSeats >= groupDiscountSeatMinimum[0]) {
                return groupDiscount[1];
            } else if (numberOfSeats >= groupDiscountSeatMinimum[1]) {
                return groupDiscount[0];
            }
        }
        return 0;
    }

    /**
     * Checks if it is discount night for a particular show time
     *
     * @param movieTimeSlot show time to check for discount night
     * @return a boolean stating discount night eligibility
     */
    private boolean isDiscountNight(LocalDateTime movieTimeSlot) {
        return eligibleDiscountNight.contains(movieTimeSlot);
    }

    /**
     * Gets discount night pricing
     *
     * @return discount night pricing as a double
     */
    private double getDiscountNightPrice() {
        return discountNight;
    }
}
