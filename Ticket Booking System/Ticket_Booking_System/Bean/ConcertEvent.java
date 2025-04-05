package Ticket_Booking_System.Bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConcertEvent extends Event{

public ConcertEvent(String eventName, LocalDate date, LocalTime time, Venue venue,
            int totalSeats, double ticketPrice) {
	super(eventName, date, time, venue, totalSeats, ticketPrice, "Concert");
}

@Override
public double calculateRevenue() {
	return (totalSeats - availableSeats) * ticketPrice;
}
}
