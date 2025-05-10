package lii.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lii.hospitalmanagementsystem.model.Nurse;
import lii.hospitalmanagementsystem.model.Employee;
import lii.hospitalmanagementsystem.model.Department;
import lii.hospitalmanagementsystem.databasecrud.EmployeeCRUD;
import lii.hospitalmanagementsystem.databasecrud.DepartmentCRUD;

public class NurseDialogController {
    @FXML private TextField employeeIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneNoField;
    @FXML private ComboBox<String> rotationComboBox;
    @FXML private TextField salaryField;
    @FXML private ComboBox<Department> departmentComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean saveClicked = false;
    private Employee employee;
    private Nurse nurse;
    private final EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private final DepartmentCRUD departmentCRUD = new DepartmentCRUD();

    @FXML
    private void initialize() {
        loadDepartments();
        setupRotationComboBox();

        cancelButton.setOnAction(e -> closeDialog());
        saveButton.setOnAction(e -> handleSave());

        // Disable employee ID field and set it to read-only
        employeeIdField.setEditable(false);
        employeeIdField.setDisable(true);

        // If this is a new nurse, generate new ID
        if (employee == null && nurse == null) {
            long nextId = employeeCRUD.getLastEmployeeId() + 1;
            employeeIdField.setText(String.valueOf(nextId));
        }

        setupDepartmentComboBox();
    }

    private void setupRotationComboBox() {
        rotationComboBox.getItems().addAll("Morning", "Afternoon", "Night");
    }

    private void setupDepartmentComboBox() {
        departmentComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDepartmentName());
                }
            }
        });

        departmentComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDepartmentName());
                }
            }
        });
    }

    private void loadDepartments() {
        if (departmentComboBox != null) {
            departmentComboBox.getItems().clear();
            departmentComboBox.getItems().addAll(departmentCRUD.getAllDepartments());
        }
    }

    public void setNurseData(Employee employee, Nurse nurse) {
        this.employee = employee;
        this.nurse = nurse;

        if (employee != null) {
            // Existing nurse - show current values
            employeeIdField.setText(String.valueOf(employee.getEmployeeId()));
            firstNameField.setText(employee.getFirstName());
            surnameField.setText(employee.getSurname());
            addressField.setText(employee.getAddress());
            telephoneNoField.setText(String.valueOf(employee.getTelephoneNo()));
        } else {
            // New nurse - generate new ID
            long nextId = employeeCRUD.getLastEmployeeId() + 1;
            employeeIdField.setText(String.valueOf(nextId));
        }

        if (nurse != null) {
            rotationComboBox.setValue(nurse.getRotation());
            salaryField.setText(String.valueOf(nurse.getSalary()));
            // Find and select the department in combo box
            departmentComboBox.getItems().stream()
                    .filter(d -> d.getDepartmentCode() == nurse.getDepartmentId())
                    .findFirst()
                    .ifPresent(department -> departmentComboBox.setValue(department));
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
                    rotationComboBox.getValue() == null ||
                    salaryField.getText().isEmpty() ||
                    departmentComboBox.getValue() == null) {
                throw new IllegalArgumentException("All fields are required.");
            }

            long employeeId = Long.parseLong(employeeIdField.getText());
            long telephoneNo ;
            try {
                telephoneNo = Long.parseLong(telephoneNoField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Telephone No must be numeric.");
            }

            double salary ;
            try {
                salary = Double.parseDouble(salaryField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Salary must be numeric.");
            }

            if (employee == null) {
                employee = new Employee();
            }
            if (nurse == null) {
                nurse = new Nurse();
            }

            employee.setEmployeeId(employeeId);
            employee.setFirstName(firstNameField.getText());
            employee.setSurname(surnameField.getText());
            employee.setAddress(addressField.getText());
            employee.setTelephoneNo(telephoneNo);

            nurse.setEmployeeId(employeeId);
            nurse.setRotation(rotationComboBox.getValue());
            nurse.setSalary(salary);
            nurse.setDepartmentId(departmentComboBox.getValue().getDepartmentCode());

            saveClicked = true;
            closeDialog();
        } catch (NumberFormatException e) {
            showError("Telephone No and Salary must be numeric.");
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

    public Nurse getNurse() {
        return nurse;
    }
}