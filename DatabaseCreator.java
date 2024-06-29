import java.sql.*;

public class DatabaseCreator {
    public static void main(String[] args) {
        String dbUrl = "jdbc:derby://localhost:1527/patient_db;create=true";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            System.out.println("Database created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
