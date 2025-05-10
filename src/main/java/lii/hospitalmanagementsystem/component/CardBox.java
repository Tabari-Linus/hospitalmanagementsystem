package lii.hospitalmanagementsystem.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CardBox extends VBox {
    private final Label titleLabel;
    private final Label countLabel;

    public CardBox(String title) {
        getStyleClass().add("card-box");
        setPadding(new Insets(15));
        setSpacing(10);

        titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        countLabel = new Label("0");
        countLabel.getStyleClass().add("count-label");

        getChildren().addAll(titleLabel, countLabel);
    }

    public void setCount(long count) {
        countLabel.setText(String.valueOf(count));
    }
}