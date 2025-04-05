package Ticket_Booking_System.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import Ticket_Booking_System.Bean.*;
import Ticket_Booking_System.util.DBUtil;

public class EventServiceProviderImpl implements IEventServiceProvider {

    @Override
    public Event createEvent(String eventName, LocalDate date, LocalTime time, int totalSeats,
                             double ticketPrice, String eventType, Venue venue) {

        Event event;

        switch (eventType.toLowerCase()) {
            case "movie":
                event = new MovieEvent(eventName, date, time, venue, totalSeats, ticketPrice);
                break;
            case "concert":
                event = new ConcertEvent(eventName, date, time, venue, totalSeats, ticketPrice);
                break;
            case "sport":
                event = new SportEvent(eventName, date, time, venue, totalSeats, ticketPrice);
                break;
            default:
                throw new IllegalArgumentException("Invalid event type: " + eventType);
        }

        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO event (event_name, event_date, event_time, total_seats, available_seats, ticket_price, event_type, venue_name, venue_address) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, event.getEventName());
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setTime(3, java.sql.Time.valueOf(time));
            ps.setInt(4, totalSeats);
            ps.setInt(5, totalSeats); // available = total
            ps.setDouble(6, ticketPrice);
            ps.setString(7, eventType);
            ps.setString(8, venue.getName());
            ps.setString(9, venue.getAddress());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while inserting event into DB: " + e.getMessage());
            e.printStackTrace();
        }

        return event;
    }

    @Override
    public Event getEventByName(String eventName) {
        Event event = null;

        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM event WHERE LOWER(event_name) = LOWER(?)")) {

            ps.setString(1, eventName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("event_name");
                LocalDate date = rs.getDate("event_date").toLocalDate();
                LocalTime time = rs.getTime("event_time").toLocalTime();
                String type = rs.getString("event_type");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                double ticketPrice = rs.getDouble("ticket_price");

                String venueName = rs.getString("venue_name");
                String venueAddress = rs.getString("venue_address");
                Venue venue = new Venue(venueName, venueAddress);

                switch (type.toLowerCase()) {
                    case "movie":
                        event = new MovieEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                    case "concert":
                        event = new ConcertEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                    case "sport":
                        event = new SportEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                }

                event.setAvailableSeats(availableSeats);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching event by name: " + e.getMessage());
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
                String name = rs.getString("event_name");
                LocalDate date = rs.getDate("event_date").toLocalDate();
                LocalTime time = rs.getTime("event_time").toLocalTime();
                String type = rs.getString("event_type");
                int totalSeats = rs.getInt("total_seats");
                int availableSeats = rs.getInt("available_seats");
                double ticketPrice = rs.getDouble("ticket_price");

                String venueName = rs.getString("venue_name");
                String venueAddress = rs.getString("venue_address");
                Venue venue = new Venue(venueName, venueAddress);

                Event event;
                switch (type.toLowerCase()) {
                    case "movie":
                        event = new MovieEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                    case "concert":
                        event = new ConcertEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                    case "sport":
                        event = new SportEvent(name, date, time, venue, totalSeats, ticketPrice);
                        break;
                    default:
                        continue;
                }

                event.setAvailableSeats(availableSeats);
                events.add(event);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching events from DB: " + e.getMessage());
            e.printStackTrace();
        }

        return events;
    }
}
