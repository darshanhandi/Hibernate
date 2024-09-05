package hotelReservation;

import java.sql.*;
import java.util.Scanner;

public class ReservationService {
    private final Connection connection;

    public ReservationService(Connection connection) {
        this.connection = connection;
    }

    public void reserveRoom(Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations(guest_name, room_number, contact_number) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, guestName);
                statement.setInt(2, roomNumber);
                statement.setString(3, contactNumber);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.err.println("Reservation successful!");
                } else {
                    System.err.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewReservations() throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("Current Reservations:");
            System.out.println(
                "+------------------+--------------+----------------+----------------------+---------------------+");
            System.out.println(
                "| Reservation ID   | Guest        | Room Number    | Contact Number       | Reservation Date     |");
            System.out.println(
                "+------------------+--------------+----------------+----------------------+---------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                    reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println(
                "+------------------+--------------+----------------+----------------------+---------------------+");
        }
    }

    public void getRoomNumber(Scanner scanner) {
        try {
            System.out.println("Enter reservation ID:");
            int reservationId = scanner.nextInt();
            System.out.println("Enter guest name:");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                statement.setString(2, guestName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId + " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservation(Scanner scanner) {
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            if (!reservationExists(reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.println("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newGuestName);
                statement.setInt(2, newRoomNumber);
                statement.setString(3, newContactNumber);
                statement.setInt(4, reservationId);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.err.println("Reservation updated successfully!");
                } else {
                    System.err.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(Scanner scanner) {
        try {
            System.out.println("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.err.println("Reservation deleted successfully!");
                } else {
                    System.err.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean reservationExists(int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
