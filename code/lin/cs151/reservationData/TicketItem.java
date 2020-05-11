package lin.cs151.reservationData;

import lin.cs151.theaterReservationAvailability.SeatType;

/**
 * Holds price and quantity (number of reserved seats) for each {@link SeatType}
 *
 * @author Raymond Lin
 */
public class TicketItem {

    private SeatType seatType;
    private int quantity;
    private double unitPrice;
    private double totalCost;

    /**
     * Creates a new instance of an item
     *
     * @param seatType  type of seat as defined by {@link SeatType}
     * @param quantity  number of seats reserved as defined by {@link Reservation}
     * @param unitPrice the price for each ticket as defined by {@link lin.cs151.ticketCost.Price} and {@link lin.cs151.ticketCost.Discounts discounts}
     */
    public TicketItem(SeatType seatType, int quantity, double unitPrice) {
        this.seatType = seatType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalCost();
    }

    /**
     * Calculates the total cost for this seat type
     */
    private void calculateTotalCost() {
        this.totalCost = unitPrice * quantity;
    }

    /**
     * Gets the total cost for this seat type
     *
     * @return the total cost for this seat type
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Converts the seat type as defined in {@link SeatType} to String equivalent
     *
     * @return the String equivalent of the seat type
     */
    public String getSeatType() {
        switch (seatType) {
            case EAST_BALCONY:
                return "East Balcony";
            case SOUTH_BALCONY_LOWER:
                return "South Balcony Lower";
            case SOUTH_BALCONY_UPPER:
                return "South Balcony Upper";
            case MAIN_FLOOR_CENTER:
                return "Main Floor Center";
            case MAIN_FLOOR_RIGHT:
                return "Main Floor Right";
            case WEST_BALCONY:
                return "West Balcony";
            case MAIN_FLOOR_LEFT:
                return "Main Floor Left";
            default:
                return "";
        }
    }

    /**
     * Gets the unit cost of the item determined by {@link lin.cs151.ticketCost.Price price} and {@link lin.cs151.ticketCost.Discounts discount}
     *
     * @return the unit cost of this item
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Get the quantity of this item
     *
     * @return quantity of item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Applies eligible discounts as defined at {@link lin.cs151.ticketCost.Discounts discounts}
     *
     * @param discount the discount to apply to this item
     */
    public void applyDiscount(double discount) {
        if (discount == 20) {
            unitPrice = discount;
        } else {
            unitPrice = unitPrice - discount;
        }
        calculateTotalCost();
    }
}
