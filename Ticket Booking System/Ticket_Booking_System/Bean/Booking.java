package Ticket_Booking_System.Bean;

import java.time.*;
import java.util.List;

public class Booking {

	private int bookingId;
    private List<Customer> customerList;
    private Event event;
    private int numberOfTickets;
    private double totalCost;
    private LocalDateTime bookingDateTime;

    public Booking(int bookingId, List<Customer> customerList, Event event,
                   int numberOfTickets, double totalCost, LocalDateTime bookingDateTime) {
        this.bookingId = bookingId;
        this.customerList = customerList;
        this.event = event;
        this.numberOfTickets = numberOfTickets;
        this.totalCost = totalCost;
        this.bookingDateTime = bookingDateTime;
    }

    public int getBookingId() { return bookingId; }
    public List<Customer> getCustomerList() { return customerList; }
    public Event getEvent() { return event; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getBookingDateTime() { return bookingDateTime; }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Event: " + event.getEventName() + ", Tickets: " + numberOfTickets + ", Total Cost: Rs. " + totalCost;
    }
}
