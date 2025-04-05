package Ticket_Booking_System.Service;

import java.util.List;
import Ticket_Booking_System.Bean.Event;


import Ticket_Booking_System.Bean.Booking;
import Ticket_Booking_System.Bean.Customer;
import Ticket_Booking_System.Bean.Event;
import Ticket_Booking_System.Bean.Venue;

public interface IBookingSystemRepository {

	Event create_event(String eventName, String date, String time, int totalSeats, double ticketPrice,
            String eventType, Venue venue);

	List<Event> getEventDetails();

	int getAvailableNoOfTickets(String eventName);

	double calculate_booking_cost(int numTickets);

	Booking book_tickets(String eventName, int numTickets, List<Customer> listOfCustomer)
				throws EventNotFoundException;

	void cancel_booking(int bookingId) throws InvalidBookingIDException;

	Booking get_booking_details(int bookingId) throws InvalidBookingIDException;
}

