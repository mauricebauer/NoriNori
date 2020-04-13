package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MainWindowController {
    @FXML
    private Label stateLabel;

    @FXML
    private void solveButtonClicked() {
        stateLabel.setText("Solved successfully");
    }

    @FXML
    private void stepButtonClicked() {
        stateLabel.setText("Stepped");
    }

    @FXML
    private void clearButtonClicked() {
        stateLabel.setText("Cleared");
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

    @FXML
    private void zoomInButtonClicked() {
        stateLabel.setText("Zoomed in");
    }

    @FXML
    private void zoomOutButtonClicked() {
        stateLabel.setText("Zoomed out");
    }

    @FXML
    private void openFileButtonClicked() {
        stateLabel.setText("File loaded");
    }
}
