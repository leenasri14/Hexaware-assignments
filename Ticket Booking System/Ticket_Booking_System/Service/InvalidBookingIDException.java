package Ticket_Booking_System.Service;

public class InvalidBookingIDException extends Exception {
    public InvalidBookingIDException(String message) {
        super(message);
    }
}
