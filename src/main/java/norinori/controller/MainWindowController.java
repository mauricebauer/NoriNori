package norinori.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import norinori.model.INoriGame;
import norinori.model.NoriGame;
import norinori.model.Solver;
import norinori.model.UiHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    private final Solver solver = new Solver();
    public GridController gridController;
    public AlertController alertController;
    // FXML elements (available after initialize())
    public GridPane statisticsGrid;
    public VBox dragAndDropField;
    public Button stepButton, solveButton, clearButton, zoomInButton, zoomOutButton, saveScreenButton, openFileButton;
    public GridPane grid;
    public Label sizeLabel, regionsLabel;
    private INoriGame noriGame;
    // Variables needed for window movement
    private double xOffset = 0;
    private double yOffset = 0;

    public void setDisableButtons(boolean state) {
        stepButton.setDisable(state);
        solveButton.setDisable(state);
        clearButton.setDisable(state);
        zoomInButton.setDisable(state);
        zoomOutButton.setDisable(state);
        saveScreenButton.setDisable(state);
        openFileButton.setDisable(state);
    }

    @FXML
    private void solveButtonClicked() {
        // Don't allow to start a new solver while running
        if (solveButton.disableProperty().get())
            return;

        Thread solverThread = new Thread(() -> solver.solve(noriGame, new UiHelper(this), false), "Solver");
        solverThread.setDaemon(true);
        solverThread.start();
    }

    @FXML
    private void stepButtonClicked() {
        // Don't allow to start a new solver while running
        if (stepButton.disableProperty().get())
            return;

        new Thread(() -> solver.solve(noriGame, new UiHelper(this), true), "Solver").start();
    }

    @FXML
    private void clearButtonClicked() {
        // Don't allow to clear the game while running
        if (clearButton.disableProperty().get())
            return;

        noriGame.resetCells();
        gridController.colorCells(noriGame);
        solver.reset();
    }

    @FXML
    private void helpButtonClicked() {
        alertController.showAttributions();
    }

    @FXML
    private void zoomInButtonClicked() {
        gridController.resizeBoard(true);
        solveButton.getScene().getWindow().sizeToScene();
    }

    @FXML
    private void zoomOutButtonClicked() {
        gridController.resizeBoard(false);
        solveButton.getScene().getWindow().sizeToScene();
    }

    @FXML
    private void saveScreenButtonClicked() {
        WritableImage image = grid.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save current grid as an image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showSaveDialog(solveButton.getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (Exception e) {
                System.out.println("Could not save image!");
            }
        }
    }

    @FXML
    private void openFileButtonClicked() {
        // Don't allow to open a file while solver is running
        if (solveButton.disableProperty().get() && noriGame != null)
            return;

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
                Platform.exit();
                break;
        }
    }

    public void setStats(int boardWidth, int boardHeight, int regions) {
        statisticsGrid.setVisible(true);
        sizeLabel.setText(boardWidth + "x" + boardHeight);
        regionsLabel.setText(String.valueOf(regions));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertController = new AlertController();
        gridController = new GridController(grid);
    }

    private void readGameFromFile(String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!path.toString().endsWith(".json"))
                throw new Exception("Could not load file!");

            String content = Files.readString(path);
            noriGame = new NoriGame(content);
            solver.reset();
            gridController.createBoard(noriGame);
            grid.setVisible(true);
            dragAndDropField.setVisible(false);
            setStats(noriGame.getMaxCol() + 1, noriGame.getMaxRow() + 1, noriGame.getMaxRegion() + 1);
            setDisableButtons(false);
        } catch (Exception e) {
            grid.setVisible(false);
            dragAndDropField.setVisible(true);
            setDisableButtons(true);
            statisticsGrid.setVisible(false);
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

    @FXML
    private void attributionsClicked() {
        alertController.showAttributions();
    }

    @FXML
    private void developerClicked() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/mauricebauer").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
