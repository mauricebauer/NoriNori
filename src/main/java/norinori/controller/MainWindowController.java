package norinori.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import norinori.model.NoriGame;
import norinori.model.Solver;
import norinori.model.UiHelper;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    private final Solver solver = new Solver();
    public GridController gridController;
    public AlertController alertController;
    private NoriGame noriGame = new NoriGame();

    // Variables needed for window movement
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private VBox dragAndDropField;

    //@FXML
    //private Label stateLabel;
    @FXML
    private Button stepButton, solveButton, clearButton;//, openFileButton;

    @FXML
    private GridPane grid;

    public void setStateLabelText(String text) {
        //stateLabel.setText(text);
    }

    public void setDisableButtons(boolean state) {
        //stepButton.setDisable(state);
        //solveButton.setDisable(state);
        //clearButton.setDisable(state);
        //openFileButton.setDisable(state);
    }

    @FXML
    private void solveButtonClicked() {
        Thread solverThread = new Thread(() -> solver.solve(noriGame, new UiHelper(this), false), "Solver");
        solverThread.setDaemon(true);
        solverThread.start();
    }

    @FXML
    private void stepButtonClicked() {
        new Thread(() -> solver.solve(noriGame, new UiHelper(this), true), "Solver").start();
    }

    @FXML
    private void clearButtonClicked() {
        noriGame.resetCells();
        gridController.colorCells(noriGame);
        //stateLabel.setText("Cleared");
        solver.reset();
    }

    @FXML
    private void helpButtonClicked() {
        alertController.showHelpAlert();
    }

    @FXML
    private void zoomInButtonClicked() {
        gridController.resizeBoard(true);
        //stateLabel.getScene().getWindow().sizeToScene();
        //stateLabel.setText("Zoomed in");
    }

    @FXML
    private void zoomOutButtonClicked() {
        gridController.resizeBoard(false);
        //stateLabel.getScene().getWindow().sizeToScene();
        //stateLabel.setText("Zoomed out");
    }

    @FXML
    private void openFileButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NoriNori board with json-Format");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(solveButton.getScene().getWindow());
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
                //((Stage) stateLabel.getScene().getWindow()).close();
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertController = new AlertController();
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
            grid.setVisible(true);
            dragAndDropField.setVisible(false);
            //stateLabel.setText("File loaded");
            //((Stage) stateLabel.getScene().getWindow()).setTitle(path.getFileName() + " - NoriNori Solver");
        } catch (Exception e) {
            noriGame = new NoriGame();
            gridController.createBoard(noriGame);
            //stateLabel.setText("File not loaded");
            alertController.showFileError();
        }
        solveButton.getScene().getWindow().sizeToScene();
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

    @FXML
    private void closeButtonClicked() {
        Platform.exit();
    }

    @FXML
    private void minimizeButtonClicked(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        xOffset = ((Node) e.getSource()).getScene().getWindow().getX() - e.getScreenX();
        yOffset = ((Node) e.getSource()).getScene().getWindow().getY() - e.getScreenY();
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        ((Node) e.getSource()).getScene().getWindow().setX(e.getScreenX() + xOffset);
        ((Node) e.getSource()).getScene().getWindow().setY(e.getScreenY() + yOffset);
    }
}
