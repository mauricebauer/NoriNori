package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MainWindowController {
    @FXML
    private Label stateLabel;

    @FXML
    private void solveButtonClicked() {
        System.out.println("Solve");
    }

    @FXML
    private void stepButtonClicked() {
        System.out.println("Step");
    }

    @FXML
    private void clearButtonClicked() {
        System.out.println("Clear");
    }

    @FXML
    private void helpButtonClicked() {
        // TODO: Add the correct content (Keys and License for Icons)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help - NoriNori Solver");
        alert.setHeaderText("NoriNori Solver V0.1");
        alert.setContentText("Use the following keys...\nHere is the license...");
        alert.showAndWait();
    }
}
