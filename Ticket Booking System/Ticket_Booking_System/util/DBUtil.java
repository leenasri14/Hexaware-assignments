package Ticket_Booking_System.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/ticket_booking_system"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Tiger@123";

    private static Connection conn = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to database.");
            e.printStackTrace();
        }

    }
    

        public static Connection getDBConn() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        public static void closeConnection() {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		
}
