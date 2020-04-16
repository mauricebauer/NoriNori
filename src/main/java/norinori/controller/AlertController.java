package norinori.controller;

import javafx.scene.control.Alert;

public class AlertController {
    public void showNotSolvableAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Solving failed");
        alert.setHeaderText("Solving failed!");
        alert.setContentText("Could not solve the current NoriNori puzzle!");
        alert.showAndWait();
    }

    public void showHelpAlert() {
        // TODO: Add the correct content (Keys and License for Icons)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help - NoriNori Solver");
        alert.setHeaderText("NoriNori Solver V0.1");
        alert.setContentText("Use the following keys...\nHere is the license...");
        alert.showAndWait();
    }

    public void showFileError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed to read file");
        alert.setHeaderText("Failed to read file!");
        alert.setContentText("The file selected cannot be read correctly!");
        alert.showAndWait();
    }
}
