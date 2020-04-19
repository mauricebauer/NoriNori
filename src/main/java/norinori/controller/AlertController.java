package norinori.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertController {
    /**
     * Shows an alert that the solving of the current game was not successful.
     * The alert must be confirmed.
     */
    public void showNotSolvableAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Solving failed");
        alert.setHeaderText("Solving failed!");
        alert.setContentText("Could not solve the current NoriNori puzzle!");
        alert.showAndWait();
    }

    /**
     * Shows the attributions and keyboard shortcuts as a separate window
     */
    public void showAttributions() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AttributionsWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("NoriNori Solver - Attributions");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Could not open attributions-window");
        }
    }

    /**
     * Shows an error. Must be confirmed to continue.
     * Should be shown if the loading of a JSON file failed.
     */
    public void showFileError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed to read file");
        alert.setHeaderText("Failed to read file!");
        alert.setContentText("The file selected cannot be read correctly!");
        alert.showAndWait();
    }
}
