package Ticket_Booking_System.Bean;

import java.time.*;

public class SportEvent  extends Event{

	public SportEvent(String eventName, LocalDate date, LocalTime time, Venue venue,
            int totalSeats, double ticketPrice) {
		super(eventName, date, time, venue, totalSeats, ticketPrice, "Sport");
}

@Override
public double calculateRevenue() {
	return (totalSeats - availableSeats) * ticketPrice;
}
}
