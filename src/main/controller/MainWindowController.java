package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.model.NoriCell;
import main.model.NoriGame;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private Label stateLabel;

    @FXML
    private GridPane grid;
    private GridController gridController;

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
        if (gridController != null)
            gridController.resizeBoard(true);
        stateLabel.getScene().getWindow().sizeToScene();
        stateLabel.setText("Zoomed in");
    }

    @FXML
    private void zoomOutButtonClicked() {
        if (gridController != null)
            gridController.resizeBoard(false);
        stateLabel.getScene().getWindow().sizeToScene();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridController = new GridController(grid);

        // Add default game (for a starting point)
        NoriGame noriGame = new NoriGame();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                noriGame.getNoriCellList().add(new NoriCell(i, j, 0));
            }
        }

        gridController.createBoard(noriGame);
    }
}
