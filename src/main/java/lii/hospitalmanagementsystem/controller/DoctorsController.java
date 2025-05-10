package lii.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import lii.hospitalmanagementsystem.databasecrud.DBConnection;
import lii.hospitalmanagementsystem.view.DoctorView;
import lii.hospitalmanagementsystem.model.Employee;
import lii.hospitalmanagementsystem.model.Doctor;
import lii.hospitalmanagementsystem.databasecrud.DoctorCRUD;
import lii.hospitalmanagementsystem.databasecrud.EmployeeCRUD;
import lii.hospitalmanagementsystem.databasecrud.SpecialityCRUD;
import lii.hospitalmanagementsystem.model.Speciality;
import lii.hospitalmanagementsystem.component.CardBox;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DoctorsController {
    @FXML private TableView<DoctorView> doctorsTable;
    @FXML private TableColumn<DoctorView, Long> idColumn;
    @FXML private TableColumn<DoctorView, String> nameColumn;
    @FXML private TableColumn<DoctorView, String> addressColumn;
    @FXML private TableColumn<DoctorView, Long> phoneColumn;
    @FXML private TableColumn<DoctorView, String> specialityColumn;
    @FXML private TableColumn<DoctorView, Void> actionsColumn;
    @FXML private TextField searchField;
    @FXML private Button addDoctorBtn;
    @FXML private HBox summaryContainer;
    private CardBox totalDoctorsCard;
    private CardBox directorDoctorsCard;

    private final DoctorCRUD doctorCRUD = new DoctorCRUD();
    private final EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private ObservableList<DoctorView> doctorsList = FXCollections.observableArrayList();
    private final SpecialityCRUD specialityCRUD = new SpecialityCRUD(); // Add this field

    @FXML
    private void initialize() {

        setupTable();
        setupSummaryCards();
        setupSearch();
        setupAddButton();
        loadDoctors();
        updateSummaryCards();
    }

    private void setupSummaryCards() {
        totalDoctorsCard = new CardBox("Total Doctors");
        directorDoctorsCard = new CardBox("Director Doctors");


        Region spacer = new Region();
        spacer.setMinWidth(20);

        summaryContainer.getChildren().addAll(totalDoctorsCard, spacer, directorDoctorsCard);
    }

    private void updateSummaryCards() {

        totalDoctorsCard.setCount(doctorsList.size());


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(DISTINCT director_id) FROM department WHERE director_id IS NOT NULL")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                directorDoctorsCard.setCount(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setupTable() {
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().getEmployeeId()).asObject());
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));
        addressColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));
        phoneColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().getTelephoneNo()).asObject());
        specialityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialityName()));

        setupActionsColumn();
        doctorsTable.setItems(doctorsList);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    DoctorView doctorView = getTableView().getItems().get(getIndex());
                    setGraphic(buttons);
                    editBtn.setOnAction(e -> editDoctor(doctorView));
                    deleteBtn.setOnAction(e -> deleteDoctor(doctorView));
                }
            }
        });
    }

    private void loadDoctors() {
        doctorsList.clear();
        List<Doctor> doctors = doctorCRUD.getAllDoctors();

        for (Doctor doctor : doctors) {
            try {
                Employee employee = employeeCRUD.getEmployeeById(doctor.getEmployeeId());
                if (employee == null) {
                    System.err.println("Employee not found for Doctor ID: " + doctor.getEmployeeId());
                    continue;
                }


                String specialityName = "";
                try {
                    Speciality speciality = specialityCRUD.getSpecialityById(doctor.getSpecialityId());
                    if (speciality != null) {
                        specialityName = speciality.getName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                doctorsList.add(new DoctorView(employee, doctor, specialityName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateSummaryCards();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                doctorsTable.setItems(doctorsList);
            } else {
                ObservableList<DoctorView> filteredList = doctorsList.filtered(doctor ->
                        doctor.getFullName().toLowerCase().contains(newValue.toLowerCase()) ||
                                String.valueOf(doctor.getEmployeeId()).contains(newValue) ||
                                doctor.getSpecialityName().toLowerCase().contains(newValue.toLowerCase()) ||
                                String.valueOf(doctor.getTelephoneNo()).contains(newValue) ||
                                doctor.getAddress().toLowerCase().contains(newValue.toLowerCase())
                );
                doctorsTable.setItems(filteredList);
            }
        });
    }

    private void setupAddButton() {
        addDoctorBtn.setOnAction(e -> showDoctorDialog(null));
    }

    private void editDoctor(DoctorView doctorView) {
        showDoctorDialog(doctorView);
    }

    private void deleteDoctor(DoctorView doctorView) {
        try {

            String checkSql = "SELECT COUNT(*) FROM department WHERE director_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setLong(1, doctorView.getEmployeeId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Cannot Delete Doctor");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("This doctor is assigned as a department director. Please reassign the department before deleting.");
                    errorAlert.showAndWait();
                    return;
                }
            }

            // If not a director, proceed with deletion confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Doctor");
            alert.setHeaderText("Delete Doctor: " + doctorView.getFullName());
            alert.setContentText("Are you sure you want to delete this doctor?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (doctorCRUD.deleteDoctor(doctorView.getEmployeeId()) &&
                            employeeCRUD.deleteEmployee(doctorView.getEmployeeId())) {
                        loadDoctors();
                    }
                }
            });
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while trying to delete the doctor: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void showDoctorDialog(DoctorView doctorView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/DoctorDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(doctorView == null ? "Add Doctor" : "Edit Doctor");
            dialogStage.setScene(new Scene(loader.load()));

            DoctorDialogController controller = loader.getController();

            if (doctorView != null) {
                Employee employee = employeeCRUD.getEmployeeById(doctorView.getEmployeeId());
                Doctor doctor = new Doctor(doctorView.getEmployeeId(), doctorView.getSpecialityId());
                controller.setDoctorData(employee, doctor);
            }

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Employee employee = controller.getEmployee();
                Doctor doctor = controller.getDoctor();

                if (doctorView == null) {

                    long newEmployeeId = employeeCRUD.insertEmployee(employee);
                    doctor.setEmployeeId(newEmployeeId);
                    doctorCRUD.insertDoctor(doctor);
                } else {
                    employeeCRUD.updateEmployee(employee);
                    doctorCRUD.updateDoctor(doctor);
                }

                loadDoctors();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Could not open doctor dialog");
            errorAlert.setContentText("An error occurred while opening the dialog:\n" + e.getMessage());
            errorAlert.showAndWait();
        }
    }

}