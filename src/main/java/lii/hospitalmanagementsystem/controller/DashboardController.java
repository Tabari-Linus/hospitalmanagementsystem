package lii.hospitalmanagementsystem.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lii.hospitalmanagementsystem.databasecrud.DBConnection;
import lii.hospitalmanagementsystem.view.PatientAdmissionView;
import lii.hospitalmanagementsystem.util.getTotal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML private Label totalDoctors;
    @FXML private Label totalNurses;
    @FXML private Label totalPatients;
    @FXML private Label totalDepartments;
    @FXML private Label totalWards;
    @FXML private Label totalAdmissions;


    @FXML private TableView<PatientAdmissionView> patientTableView;
    @FXML private TableColumn<PatientAdmissionView, Long> patientIdCol;
    @FXML private TableColumn<PatientAdmissionView, String> patientNameCol;
    @FXML private TableColumn<PatientAdmissionView, Integer> wardNumberCol;
    @FXML private TableColumn<PatientAdmissionView, String> doctorNameCol;
    @FXML private TableColumn<PatientAdmissionView, String> diagnosisCol;
    @FXML private TableColumn<PatientAdmissionView, String> admissionDateCol;

    private MainDashboardController dashboardController;
    private ObservableList<PatientAdmissionView> patientAdmissions = FXCollections.observableArrayList();

    @FXML
    private void handleDoctorsClick(MouseEvent event) {
        dashboardController.loadPage("Doctors");
    }

    @FXML
    private void initialize() {
        if (patientTableView == null || patientIdCol == null || patientNameCol == null ||
                wardNumberCol == null || doctorNameCol == null || diagnosisCol == null ||
                admissionDateCol == null) {
            System.err.println("FXML injection failed for table components");
            return;
        }

        initializeTableColumns();
        Platform.runLater(() -> {
            loadDashboardData();
            loadPatientAdmissions();
        });
    }

    private void initializeTableColumns() {
        try {
            patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
            patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            wardNumberCol.setCellValueFactory(new PropertyValueFactory<>("wardNumber"));
            doctorNameCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
            admissionDateCol.setCellValueFactory(new PropertyValueFactory<>("admissionDate"));
        } catch (Exception e) {
            showAlert("Table Initialization Error", "Failed to initialize table columns: " + e.getMessage());
        }
    }

    private void loadPatientAdmissions() {
        try (Connection connection = DBConnection.getConnection()) {
            patientAdmissions.clear();

            String query = "SELECT pa.id, p.patient_id, p.first_name || ' ' || p.surname AS patient_name, " +
                    "w.ward_number, e.first_name || ' ' || e.surname AS doctor_name, " +
                    "pa.diagnosis, pa.date_admitted " +
                    "FROM patientadmission pa " +
                    "JOIN patient p ON pa.patient_id = p.patient_id " +
                    "JOIN ward w ON pa.ward_id = w.ward_id " +
                    "LEFT JOIN patienttreatment pt ON pa.id = pt.patientadmission_id " +
                    "LEFT JOIN doctor d ON pt.doctor_id = d.employee_id " +
                    "LEFT JOIN employee e ON d.employee_id = e.employee_id " +
                    "WHERE pa.date_discharged IS NULL";

            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    PatientAdmissionView admission = new PatientAdmissionView(
                            rs.getLong("patient_id"),
                            rs.getString("patient_name"),
                            rs.getInt("ward_number"),
                            rs.getString("doctor_name"),
                            rs.getString("diagnosis"),
                            rs.getDate("date_admitted").toString()
                    );
                    patientAdmissions.add(admission);
                }
            }

            patientTableView.setItems(patientAdmissions);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load patient admissions: " + e.getMessage());
        }
    }

    private void loadDashboardData() {
        // Add null checks for all labels
        if (totalDoctors == null || totalNurses == null || totalPatients == null ||
                totalDepartments == null || totalWards == null || totalAdmissions == null) {
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            updateCard(totalDoctors, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM doctor"));


            updateCard(totalNurses, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM nurse"));


            updateCard(totalPatients, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM patient"));


            updateCard(totalDepartments, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM department"));


            updateCard(totalWards, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM ward"));


            updateCard(totalAdmissions, getTotal.getTotalEntries(connection,
                    "SELECT COUNT(*) FROM patientadmission WHERE date_discharged IS NULL"));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load dashboard data: " + e.getMessage());
        }
    }

    private void updateCard(Label label, int newValue) {
        if (label == null) return;
        try {
            int oldValue = Integer.parseInt(label.getText());
            animateCountChange(label, oldValue, newValue);
        } catch (NumberFormatException e) {
            label.setText(String.valueOf(newValue));
        }
    }

    private void animateCountChange(Label label, int oldValue, int newValue) {
        IntegerProperty count = new SimpleIntegerProperty(oldValue);
        label.textProperty().bind(count.asString());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(count, oldValue)),
                new KeyFrame(Duration.millis(500), new KeyValue(count, newValue))
        );

        ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);

        timeline.setOnFinished(e -> {
            label.textProperty().unbind();
            label.setText(String.valueOf(newValue));
        });

        timeline.play();
        st.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void setDashboardController(MainDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

}