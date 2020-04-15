package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.model.BacktrackingSolver;
import main.model.ISolver;
import main.model.NoriGame;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainWindowController implements Initializable {
    private final ISolver solver = new BacktrackingSolver();
    private NoriGame noriGame = new NoriGame();
    private GridController gridController;

    @FXML
    private Label stateLabel;

    @FXML
    private GridPane grid;

    @FXML
    private void solveButtonClicked() {
        long timeStarted = System.nanoTime();
        boolean result = solver.solve(noriGame, false);
        long timeStopped = System.nanoTime();
        if (result) {
            long timeInMs = TimeUnit.NANOSECONDS.toMillis(timeStopped - timeStarted);
            stateLabel.setText("Solved successfully (" + timeInMs + " ms)");
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
        if (solver.solve(noriGame, true)) {
            stateLabel.setText(noriGame.findUnmarkedCell() != null ? "Stepped" : "Solved successfully");
        } else {
            stateLabel.setText("Stepped backwards");
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
        gridController.resizeBoard(true);
        stateLabel.getScene().getWindow().sizeToScene();
        stateLabel.setText("Zoomed in");
    }

    @FXML
    private void zoomOutButtonClicked() {
        gridController.resizeBoard(false);
        stateLabel.getScene().getWindow().sizeToScene();
        stateLabel.setText("Zoomed out");
    }

    @FXML
    private void openFileButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NoriNori board with json-Format");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(stateLabel.getScene().getWindow());
        if (file != null) {
            readGameFromFile(file.getAbsolutePath());
        }
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

    private void readGameFromFile(String filePath) {
        try {
            Path path = Path.of(filePath);
            String content = Files.readString(path);
            noriGame = new NoriGame(content);
            solver.reset();
            gridController.createBoard(noriGame);
            stateLabel.setText("File loaded");
            ((Stage) stateLabel.getScene().getWindow()).setTitle(path.getFileName() + " - NoriNori Solver");
        } catch (Exception e) {
            noriGame = new NoriGame();
            gridController.createBoard(noriGame);
            stateLabel.setText("File not loaded");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to read file");
            alert.setHeaderText("Failed to read file!");
            alert.setContentText("The file selected cannot be read correctly!");
            alert.showAndWait();
        }
        stateLabel.getScene().getWindow().sizeToScene();
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    @FXML
    private void onDragDropped(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            String path = dragEvent.getDragboard().getFiles().get(0).getAbsolutePath();
            dragEvent.setDropCompleted(true);
            readGameFromFile(path);
        }
        dragEvent.consume();
    }
}
