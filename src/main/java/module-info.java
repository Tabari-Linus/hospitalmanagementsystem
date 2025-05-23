module lii.hospitalmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens lii.hospitalmanagementsystem to javafx.fxml;
    exports lii.hospitalmanagementsystem;
    exports lii.hospitalmanagementsystem.controller;
    opens lii.hospitalmanagementsystem.controller to javafx.fxml;
    exports lii.hospitalmanagementsystem.model;
    opens lii.hospitalmanagementsystem.model to javafx.fxml;
    exports lii.hospitalmanagementsystem.view;
    opens lii.hospitalmanagementsystem.view to javafx.fxml;
}