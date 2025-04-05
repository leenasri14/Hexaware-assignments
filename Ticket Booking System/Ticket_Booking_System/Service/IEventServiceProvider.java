package Ticket_Booking_System.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import Ticket_Booking_System.Bean.*;

public interface IEventServiceProvider {
    Event createEvent(String eventName, LocalDate date, LocalTime time, int totalSeats,
                      double ticketPrice, String eventType, Venue venue);

    List<Event> getEventDetails();

    Event getEventByName(String eventName);  // Ensure this is declared in the interface
}
