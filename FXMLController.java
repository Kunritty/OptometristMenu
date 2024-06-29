import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {
    @FXML
    private TableView<PatientRecord> tableView;

    @FXML
    private TableColumn<PatientRecord, String> nameColumn;

    @FXML
    private TableColumn<PatientRecord, String> dateColumn;

    @FXML
    private TableColumn<PatientRecord, Double> sphereRightColumn;

    @FXML
    private TableColumn<PatientRecord, Double> sphereLeftColumn;

    @FXML
    private TableColumn<PatientRecord, Double> cylinderRightColumn;

    @FXML
    private TableColumn<PatientRecord, Double> cylinderLeftColumn;

    public void setPatientRecords(ObservableList<PatientRecord> records) {
        tableView.setItems(records);
    }

    @FXML
    public void initialize() {
        System.out.println("FXMLController initialize() method called");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        sphereRightColumn.setCellValueFactory(new PropertyValueFactory<>("sphereRight"));
        sphereLeftColumn.setCellValueFactory(new PropertyValueFactory<>("sphereLeft"));
        cylinderRightColumn.setCellValueFactory(new PropertyValueFactory<>("cylinderRight"));
        cylinderLeftColumn.setCellValueFactory(new PropertyValueFactory<>("cylinderLeft"));
    }
}
