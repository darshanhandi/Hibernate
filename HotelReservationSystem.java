package hotelReservation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelReservationSystem {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            ReservationService reservationService = new ReservationService(connection);

            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reservationService.reserveRoom(scanner);
                        break;
                    case 2:
                        reservationService.viewReservations();
                        break;
                    case 3:
                        reservationService.getRoomNumber(scanner);
                        break;
                    case 4:
                        reservationService.updateReservation(scanner);
                        break;
                    case 5:
                        reservationService.deleteReservation(scanner);
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.err.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void exit() throws InterruptedException {
        System.out.println("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.println(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.err.println("Thank you for using the Hotel Reservation System!");
    }
}
