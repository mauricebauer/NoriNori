package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case F1:
                helpButtonClicked();
                break;
            case F2:
                openFileButtonClicked();
                break;
            case F5:
                stepButtonClicked();
                break;
            case F6:
                solveButtonClicked();
                break;
            case F7:
                clearButtonClicked();
                break;
            case F8:
                ((Stage) stateLabel.getScene().getWindow()).close();
                break;
        }
    }
}
