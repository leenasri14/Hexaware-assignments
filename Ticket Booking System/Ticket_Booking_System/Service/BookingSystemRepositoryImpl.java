package Ticket_Booking_System.Service;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Ticket_Booking_System.Bean.*;
import Ticket_Booking_System.util.DBUtil;

public class BookingSystemRepositoryImpl implements IBookingSystemRepository {

	@Override
	public Event create_event(String eventName, String date, String time, int totalSeats,
	                          double ticketPrice, String eventType, Venue venue) {
	    LocalDate eventDate = LocalDate.parse(date);
	    LocalTime eventTime = LocalTime.parse(time);
	    Event event;

	    switch (eventType.toLowerCase()) {
	        case "movie":
	            event = new MovieEvent(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice);
	            break;
	        case "concert":
	            event = new ConcertEvent(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice);
	            break;
	        case "sport":
	            event = new SportEvent(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice);
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid event type: " + eventType);
	    }

	    try (Connection conn = DBUtil.getDBConn();
	         PreparedStatement stmt = conn.prepareStatement(
	                 "INSERT INTO event (name, date, time, event_type, total_seats, available_seats, ticket_price, venue_name, venue_address) " +
	                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

	        stmt.setString(1, event.getEventName());
	        stmt.setDate(2, java.sql.Date.valueOf(event.getDate()));
	        stmt.setTime(3, java.sql.Time.valueOf(event.getTime()));
	        stmt.setString(4, event.getEventType());
	        stmt.setInt(5, event.getTotalSeats());
	        stmt.setInt(6, event.getAvailableSeats());
	        stmt.setDouble(7, event.getTicketPrice());
	        stmt.setString(8, event.getVenue().getName());
	        stmt.setString(9, event.getVenue().getAddress());

	        int rowsInserted = stmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Event created successfully: " + event.getEventName());
	        } else {
	            System.out.println("Failed to insert event into the database.");
	        }

	    } catch (SQLException e) {
	        System.out.println("Error inserting event into database: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return event;
	}

    @Override
    public List<Event> getEventDetails() {
    	List<Event> events = new ArrayList<>();

        try (Connection conn = DBUtil.getDBConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM event")) {

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("event_type");
                double price = rs.getDouble("ticket_price");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime time = rs.getTime("time").toLocalTime();
                String venueName = rs.getString("venue_name");
                String venueAddress = rs.getString("venue_address");

                Venue venue = new Venue(venueName, venueAddress);
                Event event = null;

                if ("Movie".equalsIgnoreCase(type)) {
                    event = new MovieEvent(name, date, time, venue, totalSeats, price);
                } else if ("Concert".equalsIgnoreCase(type)) {
                    event = new ConcertEvent(name, date, time, venue, totalSeats, price);
                } else if ("Sport".equalsIgnoreCase(type)) {
                    event = new SportEvent(name, date, time, venue, totalSeats, price);
                }

                if (event != null) {
                    event.setAvailableSeats(availableSeats);
                    events.add(event);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) {
    	int available = 0;
        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement stmt = conn.prepareStatement("SELECT available_seats FROM event WHERE name = ?")) {
            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                available = rs.getInt("available_seats");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    @Override
    public double calculate_booking_cost(int numTickets) {
        return 0.0;
    }

    @Override
    public Booking book_tickets(String eventName, int numTickets, List<Customer> listOfCustomer) {
    	Booking booking = null;
        try (Connection conn = DBUtil.getDBConn()) {
            conn.setAutoCommit(false);

            // Get event details
            PreparedStatement eventStmt = conn.prepareStatement(
                    "SELECT * FROM event WHERE name = ?");
            eventStmt.setString(1, eventName);
            ResultSet rs = eventStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Event not found.");
                return null;
            }

            int eventId = rs.getInt("id");
            String eventType = rs.getString("event_type");
            int availableSeats = rs.getInt("available_seats");
            double ticketPrice = rs.getDouble("ticket_price");
            String venueName = rs.getString("venue_name");
            String venueLocation = rs.getString("venue_location");

            if (availableSeats < numTickets) {
                System.out.println("Not enough seats available.");
                return null;
            }

            Venue venue = new Venue(venueName, venueLocation);

            LocalDate date = rs.getDate("date").toLocalDate();
            LocalTime time = rs.getTime("time").toLocalTime();
            Event event;

            switch (eventType.toLowerCase()) {
                case "movie":
                    event = new MovieEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                case "concert":
                    event = new ConcertEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                case "sport":
                    event = new SportEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                default:
                    System.out.println("Invalid event type in DB.");
                    return null;
            }

            double totalCost = numTickets * ticketPrice;

            PreparedStatement bookingStmt = conn.prepareStatement(
                    "INSERT INTO booking (event_id, number_of_tickets, total_cost, booking_time) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            bookingStmt.setInt(1, eventId);
            bookingStmt.setInt(2, numTickets);
            bookingStmt.setDouble(3, totalCost);
            bookingStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            bookingStmt.executeUpdate();

            ResultSet generatedKeys = bookingStmt.getGeneratedKeys();
            int bookingId = 0;
            if (generatedKeys.next()) {
                bookingId = generatedKeys.getInt(1);
            }

            PreparedStatement customerStmt = conn.prepareStatement(
                    "INSERT INTO customer (booking_id, name, email, phone) VALUES (?, ?, ?, ?)");
            for (Customer customer : listOfCustomer) {
                customerStmt.setInt(1, bookingId);
                customerStmt.setString(2, customer.getCustomerName());
                customerStmt.setString(3, customer.getEmail());
                customerStmt.setString(4, customer.getPhoneNumber());
                customerStmt.addBatch();
            }
            customerStmt.executeBatch();

            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE event SET available_seats = available_seats - ? WHERE id = ?");
            updateStmt.setInt(1, numTickets);
            updateStmt.setInt(2, eventId);
            updateStmt.executeUpdate();

            conn.commit();

            booking = new Booking(bookingId, listOfCustomer, event, numTickets, totalCost, LocalDateTime.now());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return booking;
    }

    @Override
    public void cancel_booking(int bookingId) {
    	try (Connection conn = DBUtil.getDBConn()) {
            conn.setAutoCommit(false);

            PreparedStatement infoStmt = conn.prepareStatement(
                    "SELECT event_id, number_of_tickets FROM booking WHERE id = ?");
            infoStmt.setInt(1, bookingId);
            ResultSet rs = infoStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Booking not found.");
                return;
            }

            int eventId = rs.getInt("event_id");
            int numTickets = rs.getInt("number_of_tickets");

            PreparedStatement deleteCust = conn.prepareStatement(
                    "DELETE FROM customer WHERE booking_id = ?");
            deleteCust.setInt(1, bookingId);
            deleteCust.executeUpdate();

            PreparedStatement deleteBooking = conn.prepareStatement(
                    "DELETE FROM booking WHERE id = ?");
            deleteBooking.setInt(1, bookingId);
            deleteBooking.executeUpdate();

            PreparedStatement updateSeats = conn.prepareStatement(
                    "UPDATE event SET available_seats = available_seats + ? WHERE id = ?");
            updateSeats.setInt(1, numTickets);
            updateSeats.setInt(2, eventId);
            updateSeats.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking get_booking_details(int bookingId) {
    	Booking booking = null;
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBUtil.getDBConn()) {

            PreparedStatement bookingStmt = conn.prepareStatement(
                    "SELECT b.*, e.* FROM booking b JOIN event e ON b.event_id = e.id WHERE b.id = ?");
            bookingStmt.setInt(1, bookingId);
            ResultSet rs = bookingStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Booking not found.");
                return null;
            }

            String eventName = rs.getString("name");
            String eventType = rs.getString("event_type");
            int availableSeats = rs.getInt("available_seats");
            double ticketPrice = rs.getDouble("ticket_price");
            String venueName = rs.getString("venue_name");
            String venueLocation = rs.getString("venue_location");

            Venue venue = new Venue(venueName, venueLocation);
            LocalDate date = rs.getDate("date").toLocalDate();
            LocalTime time = rs.getTime("time").toLocalTime();
            LocalDateTime bookingDateTime = rs.getTimestamp("booking_time").toLocalDateTime();

            Event event;
            switch (eventType.toLowerCase()) {
                case "movie":
                    event = new MovieEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                case "concert":
                    event = new ConcertEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                case "sport":
                    event = new SportEvent(eventName, date, time, venue, availableSeats, ticketPrice);
                    break;
                default:
                    System.out.println("Invalid event type.");
                    return null;
            }

            int numTickets = rs.getInt("number_of_tickets");
            double totalCost = rs.getDouble("total_cost");

            PreparedStatement custStmt = conn.prepareStatement(
                    "SELECT name, email, phone FROM customer WHERE booking_id = ?");
            custStmt.setInt(1, bookingId);
            ResultSet rsCust = custStmt.executeQuery();

            while (rsCust.next()) {
                String name = rsCust.getString("name");
                String email = rsCust.getString("email");
                String phone = rsCust.getString("phone");
                customers.add(new Customer(name, email, phone));
            }

            booking = new Booking(bookingId, customers, event, numTickets, totalCost, bookingDateTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return booking;
    }
}
