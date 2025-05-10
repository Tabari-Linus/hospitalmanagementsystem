package lii.hospitalmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lii.hospitalmanagementsystem.databasecrud.PatientCRUD;
import lii.hospitalmanagementsystem.model.Patient;
import lii.hospitalmanagementsystem.view.PatientView;

import java.sql.SQLException;

public class PatientsController {
    @FXML private TableView<PatientView> patientsTable;
    @FXML private TableColumn<PatientView, Long> idColumn;
    @FXML private TableColumn<PatientView, String> firstNameColumn;
    @FXML private TableColumn<PatientView, String> surnameColumn;
    @FXML private TableColumn<PatientView, String> addressColumn;
    @FXML private TableColumn<PatientView, Long> phoneColumn;
    @FXML private TableColumn<PatientView, Void> actionsColumn;
    @FXML private Button addButton;

    private final PatientCRUD patientCRUD = new PatientCRUD();
    private final ObservableList<PatientView> patientList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTable();
        setupAddButton();
        loadPatients();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephoneNo"));

        setupActionsColumn();
        patientsTable.setItems(patientList);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit" );

            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    PatientView patient = getTableView().getItems().get(getIndex());
                    showPatientDialog(patient);
                });

                deleteButton.setOnAction(event -> {
                    PatientView patient = getTableView().getItems().get(getIndex());
                    deletePatient(patient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void setupAddButton() {
        addButton.setOnAction(e -> showPatientDialog(null));
    }

    private void loadPatients() {
        patientList.clear();
        patientCRUD.getAllPatients().forEach(patient ->
                patientList.add(new PatientView(patient))
        );
    }

    private void deletePatient(PatientView patientView) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Patient");
        confirmation.setHeaderText("Delete Patient and Admission Records");
        confirmation.setContentText("This will delete the patient and all associated admission records. Do you want to proceed?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (patientCRUD.deletePatient(patientView.getPatientId())) {
                        loadPatients();
                    }
                } catch (SQLException e) {
                    showError("Cannot Delete Patient", e.getMessage());
                }
            }
        });
    }

    private void showPatientDialog(PatientView patientView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitalmanagementsystem/view/PatientDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(patientView == null ? "Add Patient" : "Edit Patient");
            dialogStage.setScene(new Scene(loader.load()));

            PatientDialogController controller = loader.getController();
            if (patientView != null) {
                Patient patient = new Patient(
                        patientView.getPatientId(),
                        patientView.getFirstName(),
                        patientView.getSurname(),
                        patientView.getAddress(),
                        patientView.getTelephoneNo()
                );
                controller.setPatient(patient);
            } else {

                Patient newPatient = new Patient();
                controller.setPatient(newPatient);
                controller.hidePatientIdField();
            }

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Patient patient = controller.getPatient();
                try {
                    if (patientView == null) {
                        patientCRUD.insertPatient(patient);
                    } else {
                        patientCRUD.updatePatient(patient);
                    }
                    loadPatients();
                } catch (RuntimeException e) {
                    showError("Database Error", "Failed to save patient: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            showError("Dialog Error", "Failed to open patient dialog: " + e.getMessage());
        }
    }

    private void showError(String header, String content) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(header);
        error.setContentText(content);
        error.showAndWait();
    }
}