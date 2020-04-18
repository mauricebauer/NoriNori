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
        Platform.runLater(() -> controller.setDisableButtons(true));
    }

    @Override
    public void finishedSolving(INoriGame noriGame, boolean stepping) {
        finishedSolving(noriGame, stepping, false);
    }

    @Override
    public void finishedSolving(INoriGame noriGame, boolean stepping, boolean steppingResult) {
        if (!stepping && !noriGame.isSolved())
            Platform.runLater(() -> controller.alertController.showNotSolvableAlert());

        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
        Platform.runLater(() -> controller.setDisableButtons(false));
    }
}
