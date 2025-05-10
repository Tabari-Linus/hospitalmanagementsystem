package lii.hospitaltrial.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lii.hospitaltrial.model.Doctor;
import lii.hospitaltrial.model.Employee;
import lii.hospitaltrial.model.Speciality;
import lii.hospitaltrial.databasecrud.EmployeeCRUD;
import lii.hospitaltrial.databasecrud.SpecialityCRUD;

public class DoctorDialogController {
    @FXML private TextField employeeIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneNoField;
    @FXML private ComboBox<Speciality> specialityComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean saveClicked = false;
    private Employee employee;
    private Doctor doctor;
    private final EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private final SpecialityCRUD specialityCRUD = new SpecialityCRUD();

    @FXML
    private void initialize() {

        loadSpecialities();

        cancelButton.setOnAction(e -> closeDialog());
        saveButton.setOnAction(e -> handleSave());

        // Disable employee ID field and set it to read-only
        employeeIdField.setEditable(false);
        employeeIdField.setDisable(true);

        // If this is a new doctor (employee and doctor are null), generate new ID
        if (employee == null && doctor == null) {
            long nextId = employeeCRUD.getLastEmployeeId() + 1;
            employeeIdField.setText(String.valueOf(nextId));
        }

        specialityComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Speciality item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        specialityComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Speciality item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void loadSpecialities() {
        if (specialityComboBox != null) {
            specialityComboBox.getItems().clear();
            specialityComboBox.getItems().addAll(specialityCRUD.getAllSpecialities());
        }
    }

    public void setDoctorData(Employee employee, Doctor doctor) {
        this.employee = employee;
        this.doctor = doctor;

        if (employee != null) {
            // Existing doctor - show current values
            employeeIdField.setText(String.valueOf(employee.getEmployeeId()));
            firstNameField.setText(employee.getFirstName());
            surnameField.setText(employee.getSurname());
            addressField.setText(employee.getAddress());
            telephoneNoField.setText(String.valueOf(employee.getTelephoneNo()));
        } else {
            // New doctor - generate new ID
            long nextId = employeeCRUD.getLastEmployeeId() + 1;
            employeeIdField.setText(String.valueOf(nextId));
        }

        if (doctor != null) {
            // Find and select the specialty in combo box
            specialityComboBox.getItems().stream()
                    .filter(s -> s.getSpecialityId() == doctor.getSpecialityId())
                    .findFirst()
                    .ifPresent(speciality -> specialityComboBox.setValue(speciality));
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    private void handleSave() {
        try {
            if (firstNameField.getText().isEmpty() ||
                    surnameField.getText().isEmpty() ||
                    addressField.getText().isEmpty() ||
                    telephoneNoField.getText().isEmpty() ||
                    specialityComboBox.getValue() == null) {
                throw new IllegalArgumentException("All fields are required.");
            }
            long employeeId;
            try {
                employeeId = Long.parseLong(employeeIdField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Employee ID must be numeric.");
            }
            long telephoneNo;
            try{
                telephoneNo = Long.parseLong(telephoneNoField.getText());
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Telephone No must be numeric.");
            }


            if (employee == null) {
                employee = new Employee();
            }
            if (doctor == null) {
                doctor = new Doctor();
            }

            employee.setEmployeeId(employeeId);
            employee.setFirstName(firstNameField.getText());
            employee.setSurname(surnameField.getText());
            employee.setAddress(addressField.getText());
            employee.setTelephoneNo(telephoneNo);

            doctor.setEmployeeId(employeeId);
            doctor.setSpecialityId(specialityComboBox.getValue().getSpecialityId());

            saveClicked = true;
            closeDialog();
        } catch (NumberFormatException e) {
            showError("Telephone No and Speciality ID must be numeric.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Employee getEmployee() {
        return employee;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}