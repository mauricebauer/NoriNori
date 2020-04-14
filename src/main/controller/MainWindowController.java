package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.model.BacktrackingSolver;
import main.model.ISolver;
import main.model.NoriGame;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    private final NoriGame noriGame = new NoriGame(true);
    private final ISolver solver = new BacktrackingSolver();

    @FXML
    private Label stateLabel;

    @FXML
    private GridPane grid;
    private GridController gridController;

    @FXML
    private void solveButtonClicked() {
        if (solver.solve(noriGame.getNoriCellList(), false)) {
            stateLabel.setText("Solved successfully");
        } else {
            stateLabel.setText("Solving failed");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Solving failed");
            alert.setHeaderText("Solving failed!");
            alert.setContentText("Could not solve the current NoriNori puzzle!");
            alert.showAndWait();
        }
        gridController.colorCells(noriGame);
    }

    @FXML
    private void stepButtonClicked() {
        if (solver.solve(noriGame.getNoriCellList(), true)) {
            stateLabel.setText("Stepped successfully");
            solver.reset();
        } else {
            stateLabel.setText("Stepping failed");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Solving failed");
            alert.setHeaderText("Solving failed!");
            alert.setContentText("Could not solve the current NoriNori puzzle!");
            alert.showAndWait();
        }
        gridController.colorCells(noriGame);
    }

    @FXML
    private void clearButtonClicked() {
        noriGame.resetCells();
        gridController.colorCells(noriGame);
        stateLabel.setText("Cleared");
        solver.reset();
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
        // TODO: Replace noriGame with correct loaded Game
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
        gridController.createBoard(noriGame);
    }
}
