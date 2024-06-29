import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditRecord {
    private TableView<PatientRecord> tableView;

    public EditRecord(TableView<PatientRecord> tableView) {
        this.tableView = tableView;
    }

    public void showEditDialog(PatientRecord record) {
        // Create a dialog for editing a record
        Dialog<PatientRecord> dialog = new Dialog<>();
        dialog.setTitle("Edit Record");
        dialog.setHeaderText("Edit patient details");

        // Set the button types
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Create labels and text fields for patient details
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField(record.getName());
        Label dateLabel = new Label("Date:");
        TextField dateTextField = new TextField(record.getDate());
        Label sphereRightLabel = new Label("Sphere Right:");
        TextField sphereRightTextField = new TextField(String.valueOf(record.getSphereRight()));
        Label sphereLeftLabel = new Label("Sphere Left:");
        TextField sphereLeftTextField = new TextField(String.valueOf(record.getSphereLeft()));
        Label cylinderRightLabel = new Label("Cylinder Right:");
        TextField cylinderRightTextField = new TextField(String.valueOf(record.getCylinderRight()));
        Label cylinderLeftLabel = new Label("Cylinder Left:");
        TextField cylinderLeftTextField = new TextField(String.valueOf(record.getCylinderLeft()));

        // Create layout for patient details
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(nameLabel, nameTextField, dateLabel, dateTextField, sphereRightLabel,
                sphereRightTextField, sphereLeftLabel, sphereLeftTextField, cylinderRightLabel,
                cylinderRightTextField, cylinderLeftLabel, cylinderLeftTextField);
        dialog.getDialogPane().setContent(vbox);

        // Enable or disable the save button based on the text field values
        dialog.getDialogPane().lookupButton(saveButton).setDisable(true);
        nameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));
        dateTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));
        sphereRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));
        sphereLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));
        cylinderRightTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));
        cylinderLeftTextField.textProperty().addListener((observable, oldValue, newValue) ->
                dialog.getDialogPane().lookupButton(saveButton).setDisable(newValue.trim().isEmpty()));

        // Convert the text field values to appropriate data types and update the record
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                record.setName(nameTextField.getText());
                record.setDate(dateTextField.getText());
                record.setSphereRight(Double.parseDouble(sphereRightTextField.getText()));
                record.setSphereLeft(Double.parseDouble(sphereLeftTextField.getText()));
                record.setCylinderRight(Double.parseDouble(cylinderRightTextField.getText()));
                record.setCylinderLeft(Double.parseDouble(cylinderLeftTextField.getText()));
                return record;
            }
            return null;
        });

        // Show the dialog and update the record in the database and table view
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait().ifPresent(updatedRecord -> {
            // Update the record in the table view
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            tableView.getItems().set(selectedIndex, updatedRecord);
            tableView.getSelectionModel().select(selectedIndex);
        });
    }
}
