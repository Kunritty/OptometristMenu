import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class PatientDBViewer {
    private static final String DB_URL = "jdbc:derby://localhost:1527/patient_db;create=true";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public static ObservableList<PatientRecord> getPatientRecords() {
        ObservableList<PatientRecord> patientRecords = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patients");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                double rightSphere = resultSet.getDouble("right_sphere");
                double leftSphere = resultSet.getDouble("left_sphere");
                double rightCylinder = resultSet.getDouble("right_cylinder");
                double leftCylinder = resultSet.getDouble("left_cylinder");

                PatientRecord record = new PatientRecord(name, date, rightSphere, leftSphere, rightCylinder, leftCylinder);
                patientRecords.add(record);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientRecords;
    }
}