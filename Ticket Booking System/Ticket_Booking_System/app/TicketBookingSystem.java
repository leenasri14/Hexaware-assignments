package Ticket_Booking_System.app;

import Ticket_Booking_System.Bean.*;
import Ticket_Booking_System.Service.*;
import Ticket_Booking_System.util.DBUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TicketBookingSystem {

    private static Scanner scanner = new Scanner(System.in);
    private static IBookingSystemServiceProvider bookingService = new BookingSystemServiceProviderImpl();
    private static IEventServiceProvider eventService = new EventServiceProviderImpl();

    public static void main(String[] args) {
        System.out.println("========== Welcome to EventEase - Ticket Booking System ==========");

        int choice;
        do {
            System.out.println("\n1. Create Event");
            System.out.println("2. View All Events");
            System.out.println("3. Book Tickets");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking Details");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    viewAllEvents();
                    break;
                case 3:
                    bookTickets();
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    viewBookingDetails();
                    break;
                case 6:
                    DBUtil.closeConnection();
                    System.out.println("Thank you for using EventEase!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);
    }

    private static void createEvent() {
        try {
            System.out.print("Enter event name: ");
            String name = scanner.nextLine();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            System.out.print("Enter time (HH:MM): ");
            String timeStr = scanner.nextLine();
            System.out.print("Enter total seats: ");
            int seats = scanner.nextInt();
            System.out.print("Enter ticket price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            System.out.print("Enter event type (Movie/Concert/Sport): ");
            String type = scanner.nextLine();
            System.out.print("Enter venue name: ");
            String venueName = scanner.nextLine();
            System.out.print("Enter venue address: ");
            String venueAddress = scanner.nextLine();

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime time = LocalTime.parse(timeStr);
            Venue venue = new Venue(venueName, venueAddress);

            Event event = eventService.createEvent(name, date, time, seats, price, type, venue);
            System.out.println("Event created successfully: " + event.getEventName());
        } catch (Exception e) {
            System.out.println("Error creating event: " + e.getMessage());
        }
    }

    private static void viewAllEvents() {
        List<Event> events = eventService.getEventDetails();
        if (events == null || events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        for (Event event : events) {
            System.out.println("\n----------------------------");
            System.out.println("Event Name: " + event.getEventName());
            System.out.println("Date: " + event.getDate());
            System.out.println("Time: " + event.getTime());
            System.out.println("Venue: " + event.getVenue().getName());
            System.out.println("Available Seats: " + event.getAvailableSeats());
            System.out.println("Ticket Price: " + event.getTicketPrice());
            System.out.println("Event Type: " + event.getEventType());
        }
    }

    private static void bookTickets() {
        try {
            System.out.print("Enter event name: ");
            String eventName = scanner.nextLine();
            System.out.print("Enter number of tickets: ");
            int num = scanner.nextInt();
            scanner.nextLine();

            Customer[] customers = new Customer[num];
            for (int i = 0; i < num; i++) {
                System.out.print("Enter name of customer " + (i + 1) + ": ");
                String name = scanner.nextLine();
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter phone: ");
                String phone = scanner.nextLine();
                customers[i] = new Customer(name, email, phone);
            }

            Booking booking = bookingService.book_tickets(eventName, num, customers);
            System.out.println("Booking successful! Booking ID: " + booking.getBookingId());
        } catch (EventNotFoundException e) {
            System.out.println("Event not found: " + e.getMessage());
        }
    }

    private static void cancelBooking() {
        try {
            System.out.print("Enter booking ID to cancel: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            bookingService.cancel_booking(id);
            System.out.println("Booking cancelled successfully.");
        } catch (InvalidBookingIDException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewBookingDetails() {
        try {
            System.out.print("Enter booking ID to view: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            bookingService.get_booking_details(id);
        } catch (InvalidBookingIDException e) {
            System.out.println(e.getMessage());
        }
    }
}
