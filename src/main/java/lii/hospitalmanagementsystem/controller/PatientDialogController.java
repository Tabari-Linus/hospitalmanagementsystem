package lii.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lii.hospitalmanagementsystem.model.Patient;

public class PatientDialogController {
    @FXML private TextField patientIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneNoField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean saveClicked = false;
    private Patient patient;

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
    }

    public void hidePatientIdField() {
        if (patientIdField != null) {
            patientIdField.setVisible(false);
            patientIdField.setManaged(false); // Removes the space taken by the field
        }
        // Also hide the corresponding label if you have one
        Node idLabelNode = patientIdField.getParent().lookup("#patientIdLabel");
        if (idLabelNode instanceof Label idLabel) {
            idLabel.setVisible(false);
            idLabel.setManaged(false);
        }
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient.getPatientId() != null) {
            patientIdField.setText(String.valueOf(patient.getPatientId()));
            patientIdField.setDisable(true);
        } else {
            hidePatientIdField();
        }
        firstNameField.setText(patient.getFirstName());
        surnameField.setText(patient.getSurname());
        addressField.setText(patient.getAddress());

        // Set telephone number properly - handle 0 as valid number
        telephoneNoField.setText(String.valueOf(patient.getTelephoneNo()));
    }

    // Add this method to disable the patient ID field
    public void disablePatientIdField() {
        patientIdField.setEditable(false);
        patientIdField.setDisable(true);
        patientIdField.setStyle("-fx-opacity: 0.8;");
    }

    private void handleSave() {
        if (isInputValid()) {
            // Only set patient ID if the field is visible and not empty
            if (patientIdField.isVisible() && !patientIdField.getText().isEmpty()) {
                patient.setPatientId(Long.parseLong(patientIdField.getText()));
            }

            patient.setFirstName(firstNameField.getText());
            patient.setSurname(surnameField.getText());
            patient.setAddress(addressField.getText());

            // Handle phone number directly
            try {
                String phoneText = telephoneNoField.getText().trim();
                patient.setTelephoneNo(Long.parseLong(phoneText));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid Phone Number");
                alert.setContentText("Please enter a valid phone number.");
                alert.showAndWait();
                return;
            }

            saveClicked = true;
            closeDialog();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().isEmpty()) {
            errorMessage += "First name is required!\n";
        }
        if (surnameField.getText() == null || surnameField.getText().isEmpty()) {
            errorMessage += "Surname is required!\n";
        }
        if (addressField.getText() == null || addressField.getText().isEmpty()) {
            errorMessage += "Address is required!\n";
        }
        if (telephoneNoField.getText() == null || telephoneNoField.getText().isEmpty()) {
            errorMessage += "Phone number is required!\n";
        } else {
            try {
                Long.parseLong(telephoneNoField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage += "Phone number must be numeric!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void handleCancel() {
        saveClicked = false;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Patient getPatient() {
        return patient;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}