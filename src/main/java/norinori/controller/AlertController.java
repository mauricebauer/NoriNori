package norinori.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertController {
    public void showNotSolvableAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Solving failed");
        alert.setHeaderText("Solving failed!");
        alert.setContentText("Could not solve the current NoriNori puzzle!");
        alert.showAndWait();
    }

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

    public void showFileError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed to read file");
        alert.setHeaderText("Failed to read file!");
        alert.setContentText("The file selected cannot be read correctly!");
        alert.showAndWait();
    }
}
