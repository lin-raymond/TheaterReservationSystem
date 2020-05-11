package lin.cs151.fileManagement;

import lin.cs151.reservationData.Reservation;
import lin.cs151.reservationManager.ReservationManager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages all transactions for reservations on a designated file
 *
 * @author Raymond Lin
 */
public class ReservationFileManagement {

    private File reservationFile;

    /**
     * Parses {@link lin.cs151.reservationData.Reservation reservation} data from file in a certain format
     *
     * @param br BufferedReader to use while reading
     * @return a {@link lin.cs151.reservationData.Reservation reservation}
     * @throws IOException when file reading is interrupted unexpectedly
     */
    private Reservation parseReservationData(BufferedReader br) throws IOException {
        br.readLine();
        String confirmationNumber = br.readLine();
        String username = br.readLine();
        String dateTime = br.readLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        LocalDateTime reservationTimeSlot = LocalDateTime.parse(dateTime, formatter);
        br.readLine();
        String totalSeatsReserved = br.readLine();
        int totalNumberOfSeatsReserved = Integer.parseInt(totalSeatsReserved);
        br.readLine();
        String seatArrayAsString = br.readLine();
        String[] seatIDs = seatArrayAsString.split(", ");
        List<String> seatArray = new ArrayList<>(Arrays.asList(seatIDs));
        return new Reservation(confirmationNumber, username, totalNumberOfSeatsReserved, reservationTimeSlot, seatArray);
    }

    /**
     * Reads all {@link lin.cs151.reservationData.Reservation reservations} from file
     *
     * @return a list of {@link lin.cs151.reservationData.Reservation reservations}
     */
    public List<Reservation> readReservationFile() {
        List<Reservation> reservationListFromFile = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(reservationFile));
            String nextLine;
            nextLine = br.readLine();
            // if we reach end of file (null)
            while (nextLine != null) {
                // checks for this line "newReservation" for new reservation
                if (nextLine.compareTo("newReservation") == 0) {
                    Reservation reservation = parseReservationData(br);
                    reservationListFromFile.add(reservation);
                }
                nextLine = br.readLine();
                //System.out.println(nextLine);
            }
            br.close();
        } catch (FileNotFoundException e) {
            // pretend that file does not exit
        } catch (IOException e) {
            // pretend that file does not exist
        }
        return reservationListFromFile;
    }

    /**
     * Write all {@link lin.cs151.reservationData.Reservation reservations} from {@link lin.cs151.reservationManager.ReservationManager reservation manager}
     *
     * @param reservationManager the {@link lin.cs151.reservationManager.ReservationManager reservation manager} containing all {@link lin.cs151.reservationData.Reservation reservations}
     */
    public void writeReservation(ReservationManager reservationManager) {
        List<Reservation> reservationList = reservationManager.compileAllReservation();
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(reservationFile));
            for (Reservation r : reservationList) {
                // marks start of reservation in file
                String beginReservation = "newReservation\n";
                String reservationData = r.toString();
                // marks end of reservation in file
                String endReservation = "endReservation\n";
                bw.write(beginReservation + reservationData + endReservation);
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Could not write reservation data to file");
        }
    }

    /**
     * Loads new instance of the Reservation File Manager
     */
    public ReservationFileManagement() {
        reservationFile = new File(".\\hw1\\data", "reservation.txt");
    }
}
