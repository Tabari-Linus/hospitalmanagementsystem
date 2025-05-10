package lii.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import lii.hospitalmanagementsystem.databasecrud.EmployeeCRUD;
import lii.hospitalmanagementsystem.databasecrud.NurseCRUD;
import lii.hospitalmanagementsystem.databasecrud.DepartmentCRUD;
import lii.hospitalmanagementsystem.model.Employee;
import lii.hospitalmanagementsystem.model.Nurse;
import lii.hospitalmanagementsystem.view.NurseView;
import lii.hospitalmanagementsystem.model.Department;
import lii.hospitalmanagementsystem.component.CardBox;

import java.sql.SQLException;
import java.util.List;

public class NursesController {
    @FXML private TableView<NurseView> nursesTable;
    @FXML private TableColumn<NurseView, Long> idColumn;
    @FXML private TableColumn<NurseView, String> nameColumn;
    @FXML private TableColumn<NurseView, String> addressColumn;
    @FXML private TableColumn<NurseView, Long> phoneColumn;
    @FXML private TableColumn<NurseView, String> rotationColumn;
    @FXML private TableColumn<NurseView, String> departmentColumn;
    @FXML private TableColumn<NurseView, Void> actionsColumn;
    @FXML private TextField searchField;
    @FXML private Button addNurseBtn;
    @FXML private HBox summaryContainer;
    private CardBox totalNursesCard;

    private final NurseCRUD nurseCRUD = new NurseCRUD();
    private final EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private final DepartmentCRUD departmentCRUD = new DepartmentCRUD();
    private ObservableList<NurseView> nursesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTable();
        setupSummaryCards();
        setupSearch();
        setupAddButton();
        loadNurses();
        updateSummaryCards();
    }

    private void setupSummaryCards() {
        totalNursesCard = new CardBox("Total Nurses");
        summaryContainer.getChildren().add(totalNursesCard);
    }

    private void updateSummaryCards() {
        totalNursesCard.setCount(nursesList.size());
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
        rotationColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRotation()));
        departmentColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartmentName()));

        setupActionsColumn();
        nursesTable.setItems(nursesList);
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
                    NurseView nurseView = getTableView().getItems().get(getIndex());
                    setGraphic(buttons);
                    editBtn.setOnAction(e -> editNurse(nurseView));
                    deleteBtn.setOnAction(e -> deleteNurse(nurseView));
                }
            }
        });
    }

    private void loadNurses() {
        nursesList.clear();
        List<Nurse> nurses = nurseCRUD.getAllNurses();

        for (Nurse nurse : nurses) {
            try {
                Employee employee = employeeCRUD.getEmployeeById(nurse.getEmployeeId());
                if (employee == null) {
                    System.err.println("Employee not found for Nurse ID: " + nurse.getEmployeeId());
                    continue;
                }

                String departmentName = "";
                try {
                    Department department = departmentCRUD.getDepartmentById(nurse.getDepartmentId());
                    if (department != null) {
                        departmentName = department.getDepartmentName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                nursesList.add(new NurseView(employee, nurse, departmentName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateSummaryCards();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                nursesTable.setItems(nursesList);
            } else {
                ObservableList<NurseView> filteredList = nursesList.filtered(nurse ->
                        nurse.getFullName().toLowerCase().contains(newValue.toLowerCase()) ||
                                String.valueOf(nurse.getEmployeeId()).contains(newValue) ||
                                nurse.getDepartmentName().toLowerCase().contains(newValue.toLowerCase()) ||
                                nurse.getAddress().toLowerCase().contains(newValue.toLowerCase()) ||
                                String.valueOf(nurse.getTelephoneNo()).contains(newValue) ||
                                nurse.getRotation().toLowerCase().contains(newValue.toLowerCase())
                );
                nursesTable.setItems(filteredList);
            }
        });
    }

    private void setupAddButton() {
        addNurseBtn.setOnAction(e -> showNurseDialog(null));
    }

    private void editNurse(NurseView nurseView) {
        showNurseDialog(nurseView);
    }

    private void deleteNurse(NurseView nurseView) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Nurse");
        confirmation.setHeaderText("Delete Nurse");
        confirmation.setContentText("Are you sure you want to delete this nurse?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean deleted = nurseCRUD.deleteNurse(nurseView.getEmployeeId());
                    if (deleted) {
                        loadNurses();
                    }
                } catch (SQLException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Cannot Delete Nurse");
                    error.setContentText(e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    private void showNurseDialog(NurseView nurseView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitalmanagementsystem/view/NurseDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(nurseView == null ? "Add Nurse" : "Edit Nurse");
            dialogStage.setScene(new Scene(loader.load()));

            NurseDialogController controller = loader.getController();
            if (nurseView != null) {
                Employee employee = employeeCRUD.getEmployeeById(nurseView.getEmployeeId());
                Nurse nurse = new Nurse(
                        nurseView.getEmployeeId(),
                        nurseView.getRotation(),
                        nurseView.getSalary(),
                        nurseView.getDepartmentId()
                );
                controller.setNurseData(employee, nurse);
            }

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Employee employee = controller.getEmployee();
                Nurse nurse = controller.getNurse();

                if (nurseView == null) {
                    Long employeeId = employeeCRUD.insertEmployee(employee);
                    nurse.setEmployeeId(employeeId);
                    nurseCRUD.insertNurse(nurse);
                } else {
                    employeeCRUD.updateEmployee(employee);
                    nurseCRUD.updateNurse(nurse);
                }
                loadNurses();
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to save nurse: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}