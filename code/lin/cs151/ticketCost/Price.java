package lin.cs151.ticketCost;

import lin.cs151.theaterReservationAvailability.SeatType;

import java.util.Map;
import java.util.TreeMap;

/**
 * Handles all pricing for each seat type defined by {@link SeatType SeatType}
 *
 * @author Raymond Lin
 */
public class Price {

    private double[] prices;
    private Map<SeatType, Integer> priceMap;

    /**
     * Creates a new instance of the seat type pricing
     */
    public Price() {
        loadPrices();
        loadPriceMap();
    }

    /**
     * Loads prices into specific array
     */
    private void loadPrices() {
        prices = new double[]{35, 45, 50, 55, 40};
    }

    /**
     * Maps out seat type defined by {@link SeatType SeatType} to the price index
     */
    private void loadPriceMap() {
        this.priceMap = new TreeMap<>();
        priceMap.put(SeatType.MAIN_FLOOR_LEFT, 0);
        priceMap.put(SeatType.MAIN_FLOOR_RIGHT, 0);
        priceMap.put(SeatType.MAIN_FLOOR_CENTER, 1);
        priceMap.put(SeatType.SOUTH_BALCONY_UPPER, 2);
        priceMap.put(SeatType.SOUTH_BALCONY_LOWER, 3);
        priceMap.put(SeatType.EAST_BALCONY, 4);
        priceMap.put(SeatType.WEST_BALCONY, 4);
    }

    /**
     * Gets the price of a specific seat type
     *
     * @param seatType seat type to get price of
     * @return the price of seat type in a double
     */
    public double getPrice(SeatType seatType) {
        return prices[priceMap.get(seatType)];
    }
}
