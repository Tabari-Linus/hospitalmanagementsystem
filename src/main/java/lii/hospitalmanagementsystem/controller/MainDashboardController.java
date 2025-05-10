package lii.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainDashboardController {
    @FXML private StackPane contentArea;
    @FXML private Button dashboardBtn;
    @FXML private Button doctorsBtn;
    @FXML private Button nursesBtn;
    @FXML private Button patientsBtn;
    @FXML private Button departmentsBtn;
    @FXML private Button operationsBtn;

    @FXML
    private void initialize() {
        setupNavigation();
        loadPage("Dashboard"); // Load Dashboard.fxml initially
    }



    private void setupNavigation() {
        if (dashboardBtn != null) dashboardBtn.setOnAction(e -> loadPage("Dashboard"));
        if (doctorsBtn != null) doctorsBtn.setOnAction(e -> loadPage("Doctors"));
        if (nursesBtn != null) nursesBtn.setOnAction(e -> loadPage("Nurses"));
        if (patientsBtn != null) patientsBtn.setOnAction(e -> loadPage("Patients"));
        if (departmentsBtn != null) departmentsBtn.setOnAction(e -> loadPage("Departments"));
        if (operationsBtn != null) operationsBtn.setOnAction(e -> loadPage("Operations"));
    }

    public void loadPage(String page) {
        try {
            if (page == "Dashboard") {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/Dashboard.fxml"));
                Parent dashboard = loader.load();
                contentArea.getChildren().setAll(dashboard);
            }else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/" + page + ".fxml"));
                Node pageContent = loader.load();
                contentArea.getChildren().setAll(pageContent);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
