package lin.cs151.ticketCost;

import lin.cs151.reservationData.Reservation;
import lin.cs151.reservationData.TicketItem;
import lin.cs151.theaterReservationAvailability.SeatType;
import lin.cs151.theaterReservationAvailability.SectionSeat;
import lin.cs151.reservationData.ReservationTotalCostBreakDown;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Handles all price calculation of a {@link Reservation reservation}
 *
 * @author Raymond Lin
 */
public class ReservationPriceCalculator {

    private LocalDateTime dateAndTime;
    private int numberOfSeatsReserved;
    private List<String> seatIDs;

    private Map<SeatType, Integer> seatCount;
    private SectionSeat seatType;
    private Price priceList;
    private Discounts discounts;

    /**
     * Creates a new price calculator for a particular {@link Reservation reservation}
     *
     * @param reservation reservation of calculate price for
     */
    public ReservationPriceCalculator(Reservation reservation) {
        this.dateAndTime = reservation.getMovieTimeSlot();
        this.numberOfSeatsReserved = reservation.getTotalSeatsReserved();
        this.seatIDs = reservation.getSeatIDsAsArray();
        this.seatType = new SectionSeat();

        this.seatCount = new TreeMap<>();
        initializeSeatCount();
        this.priceList = new Price();
        this.discounts = new Discounts();
    }

    /**
     * Prepares to count the number of seat types in a {@link Reservation reservation}
     */
    private void initializeSeatCount() {
        seatCount.put(SeatType.MAIN_FLOOR_LEFT, 0);
        seatCount.put(SeatType.MAIN_FLOOR_RIGHT, 0);
        seatCount.put(SeatType.MAIN_FLOOR_CENTER, 0);
        seatCount.put(SeatType.SOUTH_BALCONY_LOWER, 0);
        seatCount.put(SeatType.SOUTH_BALCONY_UPPER, 0);
        seatCount.put(SeatType.WEST_BALCONY, 0);
        seatCount.put(SeatType.EAST_BALCONY, 0);
    }

    /**
     * Starts counting number of seat types using definitions held by using {@link SectionSeat#determineSeatTypeAndCount(Map, List) determineSeatTypeAndCount}
     */
    private void countSeatTypes() {
        seatType.determineSeatTypeAndCount(seatCount, seatIDs);
    }

    /**
     * Creates {@link TicketItem TicketItem} to hold all subtotals of each {@link Reservation reservation}
     *
     * @return a list of {@link TicketItem TicketItem} to hold all required data
     */
    private List<TicketItem> finalizeNormalCost() {
        List<TicketItem> allSeatTotals = new ArrayList<>();
        TicketItem mainFloorLeftTotalCost = new TicketItem(SeatType.MAIN_FLOOR_LEFT, seatCount.get(SeatType.MAIN_FLOOR_LEFT), priceList.getPrice(SeatType.MAIN_FLOOR_LEFT));
        allSeatTotals.add(mainFloorLeftTotalCost);
        TicketItem mainFloorRightTotalCost = new TicketItem(SeatType.MAIN_FLOOR_RIGHT, seatCount.get(SeatType.MAIN_FLOOR_RIGHT), priceList.getPrice(SeatType.MAIN_FLOOR_RIGHT));
        allSeatTotals.add(mainFloorRightTotalCost);
        TicketItem mainFloorCenterTotalCost = new TicketItem(SeatType.MAIN_FLOOR_CENTER, seatCount.get(SeatType.MAIN_FLOOR_CENTER), priceList.getPrice(SeatType.MAIN_FLOOR_CENTER));
        allSeatTotals.add(mainFloorCenterTotalCost);
        TicketItem southBalconyLowerTotalCost = new TicketItem(SeatType.SOUTH_BALCONY_LOWER, seatCount.get(SeatType.SOUTH_BALCONY_LOWER), priceList.getPrice(SeatType.SOUTH_BALCONY_LOWER));
        allSeatTotals.add(southBalconyLowerTotalCost);
        TicketItem southBalconyUpperTotalCost = new TicketItem(SeatType.SOUTH_BALCONY_UPPER, seatCount.get(SeatType.SOUTH_BALCONY_UPPER), priceList.getPrice(SeatType.SOUTH_BALCONY_UPPER));
        allSeatTotals.add(southBalconyUpperTotalCost);
        TicketItem eastBalconyTotalCost = new TicketItem(SeatType.EAST_BALCONY, seatCount.get(SeatType.EAST_BALCONY), priceList.getPrice(SeatType.EAST_BALCONY));
        allSeatTotals.add(eastBalconyTotalCost);
        TicketItem westBalconyTotalCost = new TicketItem(SeatType.WEST_BALCONY, seatCount.get(SeatType.WEST_BALCONY), priceList.getPrice(SeatType.WEST_BALCONY));
        allSeatTotals.add(westBalconyTotalCost);
        return allSeatTotals;
    }

    /**
     * Calculate the total cost for each {@link TicketItem TicketItem} and factor in discount eligibility through {@link Discounts Discounts} and stores as an easy to read break down {@link ReservationTotalCostBreakDown}
     *
     * @return a break down of prices and quantity held by {@link ReservationTotalCostBreakDown}
     */
    public ReservationTotalCostBreakDown calculateTotalPrice() {
        countSeatTypes();
        List<TicketItem> allSubTotals = finalizeNormalCost();
        ReservationTotalCostBreakDown reservationTotalCostBreakDown = new ReservationTotalCostBreakDown(allSubTotals);
        double discount = discounts.getDiscounts(dateAndTime, numberOfSeatsReserved);
        if (discount == 20) {
            reservationTotalCostBreakDown.setDiscountNightEligible();
        } else if (discount != 0) {
            reservationTotalCostBreakDown.setGroupDiscountEligible();
        }
        for (TicketItem ticketItem : allSubTotals) {
            ticketItem.applyDiscount(discount);
        }
        return reservationTotalCostBreakDown;
    }
}
