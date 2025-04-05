package Ticket_Booking_System.Service;

import Ticket_Booking_System.Bean.*;
import Ticket_Booking_System.Service.*;
import java.time.LocalDateTime;
import java.util.*;

public class BookingSystemServiceProviderImpl implements IBookingSystemServiceProvider {

    private List<Booking> bookingList = new ArrayList<>();
    private int bookingCounter = 1000;

    private IEventServiceProvider eventServiceProvider = new EventServiceProviderImpl();

    @Override
    public Booking book_tickets(String eventName, int numTickets, Customer[] customerArray) throws EventNotFoundException {
        Event event = eventServiceProvider.getEventByName(eventName); 

        if (event == null) {
            throw new EventNotFoundException("Event not found: " + eventName);
        }

        if (event.getAvailableSeats() < numTickets) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        List<Customer> customerList = Arrays.asList(customerArray);
        double totalCost = calculate_booking_cost(numTickets, event);
        event.setAvailableSeats(event.getAvailableSeats() - numTickets);


        Booking booking = new Booking(++bookingCounter, customerList, event, numTickets, totalCost, LocalDateTime.now());
        bookingList.add(booking);
        return booking;
    }

    @Override
    public void cancel_booking(int bookingId) throws InvalidBookingIDException {
        Booking bookingToCancel = null;
        for (Booking b : bookingList) {
            if (b.getBookingId() == bookingId) {
                bookingToCancel = b;
                break;
            }
        }

        if (bookingToCancel == null) {
            throw new InvalidBookingIDException("Booking ID not found: " + bookingId);
        }

        Event event = bookingToCancel.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + bookingToCancel.getNumberOfTickets());

        bookingList.remove(bookingToCancel);
    }

    @Override
    public void get_booking_details(int bookingId) throws InvalidBookingIDException {
        for (Booking b : bookingList) {
            if (b.getBookingId() == bookingId) {
                System.out.println("----- Booking Details -----");
                System.out.println("Booking ID: " + b.getBookingId());
                System.out.println("Event: " + b.getEvent().getEventName());
                System.out.println("Date: " + b.getEvent().getDate());
                System.out.println("Time: " + b.getEvent().getTime());
                System.out.println("Venue: " + b.getEvent().getVenue().getName());
                System.out.println("Number of Tickets: " + b.getNumberOfTickets());
                System.out.println("Total Cost: " + b.getTotalCost());
                System.out.println("Booking Time: " + b.getBookingDateTime());

                for (int i = 0; i < b.getCustomerList().size(); i++) {
                    Customer c = b.getCustomerList().get(i);
                    System.out.println("Customer " + (i + 1) + ": " + c.getCustomerName() + ", " + c.getEmail() + ", " + c.getPhoneNumber());
                }
                return;
            }
        }

        throw new InvalidBookingIDException("Booking ID not found: " + bookingId);
    }

    @Override
    public double calculate_booking_cost(int numTickets, Event event) {
        return numTickets * event.getTicketPrice();
    }
}
