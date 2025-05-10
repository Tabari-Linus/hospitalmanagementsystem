// Main.java
package lii.hospitalmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitalmanagementsystem/view/MainDashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800); // Set window size
        scene.getStylesheets().add(getClass().getResource("/lii/hospitalmanagementsystem/css/styles.css").toExternalForm());

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Center the window on screen
        stage.centerOnScreen();

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        // Make window maximized by default
//        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}