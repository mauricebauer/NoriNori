package norinori.model;

import javafx.application.Platform;
import norinori.controller.MainWindowController;

public class UiHelper implements IUiHelper {
    private final MainWindowController controller;

    public UiHelper(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void updateCellColor(INoriGame noriGame) {
        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
    }

    @Override
    public void startedSolving() {
        Platform.runLater(() -> controller.sidebarController.setButtons(true, true, true));
        Platform.runLater(() -> controller.setButtons(false, false, true, true));
    }

    @Override
    public void finishedSolving(INoriGame noriGame, boolean stepping) {
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
