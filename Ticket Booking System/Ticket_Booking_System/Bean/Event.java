package Ticket_Booking_System.Bean;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;
public abstract class Event {

	protected String eventName;
    protected LocalDate date;
    protected LocalTime time;
    protected Venue venue;
    protected int totalSeats;
    protected int availableSeats;
    protected double ticketPrice;
    protected String eventType;

    public Event(String eventName, LocalDate date, LocalTime time, Venue venue,
                 int totalSeats, double ticketPrice, String eventType) {
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
    }

    public String getEventName() { return eventName; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public Venue getVenue() { return venue; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public double getTicketPrice() { return ticketPrice; }
    public String getEventType() { return eventType; }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public abstract double calculateRevenue();

    @Override
    public String toString() {
        return eventName + " | " + eventType + " | Venue: " + venue + " | " + date + " " + time;
    }

}
