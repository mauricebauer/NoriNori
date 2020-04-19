package norinori.model;

import javafx.application.Platform;
import norinori.controller.MainWindowController;

public class UiHelper {
    private final MainWindowController controller;

    public UiHelper(MainWindowController controller) {
        this.controller = controller;
    }

    public void updateCellColor(NoriGame noriGame) {
        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
    }

    public void startedSolving() {
        Platform.runLater(() -> controller.sidebarController.setButtons(true, true, true));
        Platform.runLater(() -> controller.setButtons(false, false, true, true));
    }

    public void finishedSolving(NoriGame noriGame, boolean stepping) {
        if (!stepping && !noriGame.isSolved())
            Platform.runLater(() -> controller.alertController.showNotSolvableAlert());

        Platform.runLater(() -> controller.setButtons(false, false, false, false));

        if (noriGame.isSolved())
            Platform.runLater(() -> controller.sidebarController.setButtons(true, true, false));
        else
            Platform.runLater(() -> controller.sidebarController.setButtons(false, false, false));

        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
        Platform.runLater(() -> controller.gameController.isSolverRunning = false);
    }
}
