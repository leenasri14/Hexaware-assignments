package Ticket_Booking_System.Service;

import Ticket_Booking_System.Bean.Booking;
import Ticket_Booking_System.Bean.Customer;
import Ticket_Booking_System.Bean.Event;

public interface IBookingSystemServiceProvider {

	double calculate_booking_cost(int numTickets, Event event);

	Booking book_tickets(String eventName, int numTickets, Customer[] customerArray)
	        throws EventNotFoundException;

    void cancel_booking(int bookingId) throws InvalidBookingIDException;

    void get_booking_details(int bookingId) throws InvalidBookingIDException;
    
}
