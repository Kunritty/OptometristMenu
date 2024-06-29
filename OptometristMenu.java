import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Optional;

import java.sql.*;

public class OptometristMenu extends Application {

    private TableView<PatientRecord> tableView;
    private ObservableList<PatientRecord> patientRecords;
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Optometrist Menu");

        // Create combo box
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Select an option", "View All Patient Records", "Add Record", "Edit Record", "Remove Record");
        comboBox.setValue("Select an option");


        // Create submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String selectedOption = comboBox.getValue();
            if (selectedOption.equals("Select an option")) {
                // Show an alert if no option is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Option Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select an option from the menu.");
                alert.showAndWait();
            } else if (selectedOption.equals("Add Record")) {
                // Call the method to handle adding a record
                handleAddRecord();
            } else if (selectedOption.equals("View All Patient Records")) {
                // Load the patient records
                loadPatientRecords();
                showPatientRecords();
            } else if (selectedOption.equals("Edit Record")) {
                handleEditRecord();
            } else if (selectedOption.equals("Remove Record")) {
                handleRemoveRecord();
            }
        });

        // Create VBox layout and add combo box and submit button
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(comboBox, submitButton);

        // Create TableView
        tableView = new TableView<>();
        tableView.setVisible(false);

        // Create TableColumns
        

        TableColumn<PatientRecord, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<PatientRecord, Double> sphereRightColumn = new TableColumn<>("Sphere Right");
        sphereRightColumn.setCellValueFactory(new PropertyValueFactory<>("sphereRight"));

        TableColumn<PatientRecord, Double> sphereLeftColumn = new TableColumn<>("Sphere Left");
        sphereLeftColumn.setCellValueFactory(new PropertyValueFactory<>("sphereLeft"));

        TableColumn<PatientRecord, Double> cylinderRightColumn = new TableColumn<>("Cylinder Right");
        cylinderRightColumn.setCellValueFactory(new PropertyValueFactory<>("cylinderRight"));

        TableColumn<PatientRecord, Double> cylinderLeftColumn = new TableColumn<>("Cylinder Left");
        cylinderLeftColumn.setCellValueFactory(new PropertyValueFactory<>("cylinderLeft"));

        // Add TableColumns to TableView
        tableView.getColumns().addAll(nameColumn, dateColumn, sphereRightColumn, sphereLeftColumn, cylinderRightColumn, cylinderLeftColumn);

        // Create VBox layout and add combo box, submit button, and TableView
        VBox vboxWithTable = new VBox(10);
        vboxWithTable.setPadding(new Insets(10));
        vboxWithTable.getChildren().addAll(comboBox, submitButton, tableView);

        Image doctorSymbolImage = new Image("file:DoctorSymbol.jpg");
        ImageView doctorSymbolImageView = new ImageView(doctorSymbolImage);
        doctorSymbolImageView.setFitWidth(80);
        doctorSymbolImageView.setFitHeight(80);
        doctorSymbolImageView.setPreserveRatio(true);
        doctorSymbolImageView.setSmooth(true);
        doctorSymbolImageView.setCache(true);
        vboxWithTable.getChildren().add(doctorSymbolImageView);

        Scene scene = new Scene(vboxWithTable, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadPatientRecords() {
        if (patientRecords != null) {
            // Data is already loaded, no need to fetch it again
            return;
        }

        try {
            // Establish a connection to the Derby database
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/patient_db;create=true");
            Statement statement = connection.createStatement();

            // Execute a query to retrieve all patient records
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Patients");

            patientRecords = FXCollections.observableArrayList();

            // Iterate over the result set and create PatientRecord objects
            while (resultSet.next()) {
                
                String name = resultSet.getString("Name");
                String date = resultSet.getString("Date");
                double sphereRight = resultSet.getDouble("SphereRight");
                double sphereLeft = resultSet.getDouble("SphereLeft");
                double cylinderRight = resultSet.getDouble("CylinderRight");
                double cylinderLeft = resultSet.getDouble("CylinderLeft");

                patientRecords.add(new PatientRecord(name, date, sphereRight, sphereLeft, cylinderRight, cylinderLeft));
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPatientRecords() {
        if (patientRecords != null) {
            tableView.setItems(patientRecords);
            tableView.setVisible(true);
        }
    }

    private void handleAddRecord() {
        // Create a dialog for adding a new record
        Dialog<PatientRecord> dialog = new Dialog<>();
        dialog.setTitle("Add Record");
        dialog.setHeaderText("Enter patient details");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create labels and text fields for patient details
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label dateLabel = new Label("Date:");
        TextField dateTextField = new TextField();
        Label sphereRightLabel = new Label("Sphere Right:");
        TextField sphereRightTextField = new TextField();
        Label sphereLeftLabel = new Label("Sphere Left:");
        TextField sphereLeftTextField = new TextField();
        Label cylinderRightLabel = new Label("Cylinder Right:");
        TextField cylinderRightTextField = new TextField();
        Label cylinderLeftLabel = new Label("Cylinder Left:");
        TextField cylinderLeftTextField = new TextField();

        // Create layout for patient details
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(nameLabel, nameTextField, dateLabel, dateTextField, sphereRightLabel, sphereRightTextField,
                sphereLeftLabel, sphereLeftTextField, cylinderRightLabel, cylinderRightTextField, cylinderLeftLabel, cylinderLeftTextField);
        dialog.getDialogPane().setContent(vbox);

        // Enable or disable the add button based on the text field values
        dialog.getDialogPane().lookupButton(addButton).setDisable(true);
        nameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));
        dateTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));
        sphereRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));
        sphereLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));
        cylinderRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));
        cylinderLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(addButton).setDisable(newValue.trim().isEmpty()));

        // Convert the text field values to appropriate data types and create a new PatientRecord
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String name = nameTextField.getText();
                String date = dateTextField.getText();
                double sphereRight = Double.parseDouble(sphereRightTextField.getText());
                double sphereLeft = Double.parseDouble(sphereLeftTextField.getText());
                double cylinderRight = Double.parseDouble(cylinderRightTextField.getText());
                double cylinderLeft = Double.parseDouble(cylinderLeftTextField.getText());

                return new PatientRecord(name, date, sphereRight, sphereLeft, cylinderRight, cylinderLeft);
            }
            return null;
        });

        // Show the dialog and add the new record to the table view
        dialog.showAndWait().ifPresent(patientRecord -> {
            patientRecords.add(patientRecord);

            try {
                // Establish a connection to the Derby database
                connection = DriverManager.getConnection("jdbc:derby://localhost:1527/patient_db;create=true");
                Statement statement = connection.createStatement();

                // Execute a query to insert the new patient record into the database
                String insertQuery = String.format("INSERT INTO Patients (Name, Date, SphereRight, SphereLeft, CylinderRight, CylinderLeft) " +
                        "VALUES ('%s', '%s', %s, %s, %s, %s)", patientRecord.getName(), patientRecord.getDate(),
                        patientRecord.getSphereRight(), patientRecord.getSphereLeft(), patientRecord.getCylinderRight(),
                        patientRecord.getCylinderLeft());
                statement.executeUpdate(insertQuery);

                // Close the statement and connection
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    private void handleEditRecord() {
        PatientRecord selectedRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Record Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a record to edit.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog for editing the record
    Dialog<PatientRecord> dialog = new Dialog<>();
    dialog.setTitle("Edit Record");
    dialog.setHeaderText("Edit patient details");

    // Set the button types
    ButtonType editButton = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(editButton, ButtonType.CANCEL);

    // Create labels and text fields for patient details
    Label nameLabel = new Label("Name:");
    TextField nameTextField = new TextField(selectedRecord.getName());
    Label dateLabel = new Label("Date:");
    TextField dateTextField = new TextField(selectedRecord.getDate());
    Label sphereRightLabel = new Label("Sphere Right:");
    TextField sphereRightTextField = new TextField(Double.toString(selectedRecord.getSphereRight()));
    Label sphereLeftLabel = new Label("Sphere Left:");
    TextField sphereLeftTextField = new TextField(Double.toString(selectedRecord.getSphereLeft()));
    Label cylinderRightLabel = new Label("Cylinder Right:");
    TextField cylinderRightTextField = new TextField(Double.toString(selectedRecord.getCylinderRight()));
    Label cylinderLeftLabel = new Label("Cylinder Left:");
    TextField cylinderLeftTextField = new TextField(Double.toString(selectedRecord.getCylinderLeft()));

    // Create layout for patient details
    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(nameLabel, nameTextField, dateLabel, dateTextField, sphereRightLabel, sphereRightTextField,
            sphereLeftLabel, sphereLeftTextField, cylinderRightLabel, cylinderRightTextField, cylinderLeftLabel, cylinderLeftTextField);
    dialog.getDialogPane().setContent(vbox);

    // Enable or disable the edit button based on the text field values
    dialog.getDialogPane().lookupButton(editButton).setDisable(true);
    nameTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));
    dateTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));
    sphereRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));
    sphereLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));
    cylinderRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));
    cylinderLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
            dialog.getDialogPane().lookupButton(editButton).setDisable(newValue.trim().isEmpty()));

    // Convert the text field values to appropriate data types and update the selected record
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == editButton) {
            selectedRecord.setName(nameTextField.getText());
            selectedRecord.setDate(dateTextField.getText());
            selectedRecord.setSphereRight(Double.parseDouble(sphereRightTextField.getText()));
            selectedRecord.setSphereLeft(Double.parseDouble(sphereLeftTextField.getText()));
            selectedRecord.setCylinderRight(Double.parseDouble(cylinderRightTextField.getText()));
            selectedRecord.setCylinderLeft(Double.parseDouble(cylinderLeftTextField.getText()));
            return selectedRecord;
        }
        return null;
    });

    // Show the dialog and update the record in the table view
    dialog.showAndWait().ifPresent(patientRecord -> {
        tableView.refresh();

        try {
            // Establish a connection to the Derby database
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/patient_db;create=true");
            Statement statement = connection.createStatement();

            // Execute a query to update the edited patient record in the database
            String updateQuery = String.format("UPDATE Patients SET Name = '%s', Date = '%s', SphereRight = %s, SphereLeft = %s, " +
                    "CylinderRight = %s, CylinderLeft = %s WHERE Name = '%s'", patientRecord.getName(), patientRecord.getDate(),
                    patientRecord.getSphereRight(), patientRecord.getSphereLeft(), patientRecord.getCylinderRight(),
                    patientRecord.getCylinderLeft(), selectedRecord.getName());
            statement.executeUpdate(updateQuery);

            // Close the statement and connection
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
    }
    
    private void handleRemoveRecord() {
    PatientRecord selectedRecord = tableView.getSelectionModel().getSelectedItem();
    if (selectedRecord == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Record Selected");
        alert.setHeaderText(null);
        alert.setContentText("Please select a record to remove.");
        alert.showAndWait();
        return;
    }

    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirm Removal");
    confirmation.setHeaderText(null);
    confirmation.setContentText("Are you sure you want to remove the selected record?");
    Optional<ButtonType> result = confirmation.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        patientRecords.remove(selectedRecord);

        try {
            // Establish a connection to the Derby database
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/patient_db;create=true");
            Statement statement = connection.createStatement();

            // Execute a query to delete the selected patient record from the database
            String deleteQuery = String.format("DELETE FROM Patients WHERE Name = '%s'", selectedRecord.getName());
            statement.executeUpdate(deleteQuery);

            // Close the statement and connection
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

}
    