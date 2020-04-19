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

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public GridController gridController;
    public AlertController alertController;
    public SidebarController sidebarController;
    public GameController gameController;

    // FXML elements (available after initialize())
    public GridPane statisticsGrid;
    public VBox dragAndDropField;
    public Button stepButton, solveButton, clearButton, zoomInButton, zoomOutButton, saveScreenButton, openFileButton;
    public GridPane grid;
    public Label sizeLabel, regionsLabel;

    // Variables needed for window movement
    private double xOffset = 0;
    private double yOffset = 0;

    public void setButtons(boolean disableZoomInButton, boolean disableZoomOutButton, boolean disableSaveScreenButton, boolean disableOpenFileButton) {
        zoomInButton.setDisable(disableZoomInButton);
        zoomOutButton.setDisable(disableZoomOutButton);
        saveScreenButton.setDisable(disableSaveScreenButton);
        openFileButton.setDisable(disableOpenFileButton);
    }

    @FXML
    private void zoomInButtonClicked() {
        gridController.resizeBoard(true);
        grid.getScene().getWindow().sizeToScene();
    }

    @FXML
    private void zoomOutButtonClicked() {
        gridController.resizeBoard(false);
        grid.getScene().getWindow().sizeToScene();
    }

    public void saveScreenButtonClicked() {
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

    public void openFileButtonClicked() {
        if (gameController.isSolverRunning) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NoriNori board with json-Format");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(solveButton.getScene().getWindow());
        if (file != null) {
            gameController.readGameFromFile(file.getAbsolutePath());
        }
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case F1:
                alertController.showAttributions();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertController = new AlertController();
        gridController = new GridController(grid);
        sidebarController = new SidebarController(this);
        gameController = new GameController(this);
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        if (gameController.isSolverRunning) return;
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    @FXML
    private void onDragDropped(DragEvent dragEvent) {
        if (gameController.isSolverRunning) return;
        if (dragEvent.getDragboard().hasFiles()) {
            String path = dragEvent.getDragboard().getFiles().get(0).getAbsolutePath();
            dragEvent.setDropCompleted(true);
            gameController.readGameFromFile(path);
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

    public void stepButtonClicked() {
        gameController.startSolver(true);
    }

    public void solveButtonClicked() {
        gameController.startSolver(false);
    }

    public void clearButtonClicked() {
        gameController.resetGame();
    }
}
