package lin.cs151.reservationData;

import java.util.*;

/**
 * Holds all the cost data and discount data for a {@link Reservation reservation}. Cost data for each seat type is held by {@link TicketItem}.
 *
 * @author Raymond Lin
 */
public class ReservationTotalCostBreakDown {

    private boolean hasDiscount;
    private boolean isDiscountNightEligible;
    private boolean isGroupDiscountEligible;
    private List<TicketItem> allSeats;
    private double grandTotal;

    /**
     * Creates a new instance of the price breakdown for a {@link Reservation reservation}
     *
     * @param allSeatsSubTotal a list of {@link TicketItem}
     */
    public ReservationTotalCostBreakDown(List<TicketItem> allSeatsSubTotal) {
        hasDiscount = false;
        isDiscountNightEligible = false;
        isGroupDiscountEligible = false;
        allSeats = allSeatsSubTotal;
        calculateGrandTotal();
    }

    /**
     * Calculate the grand total for a {@link Reservation reservation}.
     */
    private void calculateGrandTotal() {
        for (TicketItem t : allSeats) {
            this.grandTotal += t.getTotalCost();
        }
    }

    /**
     * Sets discount eligibility.
     */
    private void setHasDiscount() {
        hasDiscount = true;
    }

    /**
     * Sets "Discount Night" eligibility.
     */
    public void setDiscountNightEligible() {
        setHasDiscount();
        isDiscountNightEligible = true;
    }

    /**
     * Sets "Group Discount" eligibility.
     */
    public void setGroupDiscountEligible() {
        setHasDiscount();
        isGroupDiscountEligible = true;
    }

    /**
     * Gets discount eligibility
     *
     * @return discount eligibility
     */
    public boolean getDiscountEligbile() {
        return hasDiscount;
    }

    /**
     * Gets eligibility for "Discount Night"
     *
     * @return "Discount Night" eligibility
     */
    public boolean getDiscountNightEligible() {
        return isDiscountNightEligible;
    }

    /**
     * Gets eligibility for "Group Discount"
     *
     * @return "Group Discount" eligibility
     */
    public boolean getGroupDiscountEligible() {
        return isGroupDiscountEligible;
    }

    /**
     * Converts the data stored in this price breakdown to a String
     *
     * @return data in this price breakdown as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cost of Reservation: \n");
        if (getDiscountEligbile()) {
            builder.append("Discount applied: ");
            if (getDiscountNightEligible()) {
                builder.append("Discount Night Promotion \n");
            } else if (getGroupDiscountEligible()) {
                builder.append("Group Discount \n");
            }
        }
        for (TicketItem t : allSeats) {
            if (t.getQuantity() != 0) {
                builder.append(t.getSeatType()).append(" : ");
                builder.append(t.getQuantity()).append(" x ").append("$").append(t.getUnitPrice()).append(" = ");
                builder.append("$").append(t.getTotalCost()).append("\n");
            }
        }
        builder.append("Grand Total: $").append(grandTotal);
        return builder.toString();
    }
}
