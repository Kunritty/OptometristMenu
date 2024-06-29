import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Label;
import java.sql.Statement;


public class AddRecord extends Application {

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeDatabase();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Name field
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        gridPane.addRow(0, nameLabel, nameField);

        // Date field
        Label dateLabel = new Label("Date:");
        TextField dateField = new TextField();
        gridPane.addRow(1, dateLabel, dateField);

        // Sphere Right field
        Label sphereRightLabel = new Label("Sphere Right:");
        TextField sphereRightField = new TextField();
        gridPane.addRow(2, sphereRightLabel, sphereRightField);

        // Sphere Left field
        Label sphereLeftLabel = new Label("Sphere Left:");
        TextField sphereLeftField = new TextField();
        gridPane.addRow(3, sphereLeftLabel, sphereLeftField);

        // Cylinder Right field
        Label cylinderRightLabel = new Label("Cylinder Right:");
        TextField cylinderRightField = new TextField();
        gridPane.addRow(4, cylinderRightLabel, cylinderRightField);

        // Cylinder Left field
        Label cylinderLeftLabel = new Label("Cylinder Left:");
        TextField cylinderLeftField = new TextField();
        gridPane.addRow(5, cylinderLeftLabel, cylinderLeftField);

        // Add button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String date = dateField.getText();
            double sphereRight = Double.parseDouble(sphereRightField.getText());
            double sphereLeft = Double.parseDouble(sphereLeftField.getText());
            double cylinderRight = Double.parseDouble(cylinderRightField.getText());
            double cylinderLeft = Double.parseDouble(cylinderLeftField.getText());

            PatientRecord patientRecord = new PatientRecord(name, date, sphereRight, sphereLeft, cylinderRight, cylinderLeft);
            insertPatientRecord(patientRecord);

            // Clear the fields after adding
            nameField.clear();
            dateField.clear();
            sphereRightField.clear();
            sphereLeftField.clear();
            cylinderRightField.clear();
            cylinderLeftField.clear();
        });

        VBox vbox = new VBox(gridPane, addButton);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Add Patient Record");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initializeDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/patient_db;create=true");

            // Create the 'PATIENTS' table if it doesn't exist
            String createTableSQL = "CREATE TABLE PATIENTS ("
                    + "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "NAME VARCHAR(255) NOT NULL,"
                    + "DATE VARCHAR(255) NOT NULL,"
                    + "SPHERE_R DOUBLE NOT NULL,"
                    + "SPHERE_L DOUBLE NOT NULL,"
                    + "CYLINDER_R DOUBLE NOT NULL,"
                    + "CYLINDER_L DOUBLE NOT NULL)";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPatientRecord(PatientRecord patientRecord) {
        String insertRecordSQL = "INSERT INTO PATIENTS (NAME, DATE, SPHERE_R, SPHERE_L, CYLINDER_R, CYLINDER_L) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertRecordSQL)) {
            statement.setString(1, patientRecord.getName());
            statement.setString(2, patientRecord.getDate());
            statement.setDouble(3, patientRecord.getSphereRight());
            statement.setDouble(4, patientRecord.getSphereLeft());
            statement.setDouble(5, patientRecord.getCylinderRight());
            statement.setDouble(6, patientRecord.getCylinderLeft());

            statement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient record added successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
