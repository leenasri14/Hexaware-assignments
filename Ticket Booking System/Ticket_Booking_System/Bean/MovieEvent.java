package Ticket_Booking_System.Bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovieEvent extends Event{

	public MovieEvent(String eventName, LocalDate date, LocalTime time, Venue venue,
            int totalSeats, double ticketPrice) {
super(eventName, date, time, venue, totalSeats, ticketPrice, "Movie");
}

@Override
public double calculateRevenue() {
return (totalSeats - availableSeats) * ticketPrice;
}
}
