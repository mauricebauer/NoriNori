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
        Platform.runLater(() -> controller.setStateLabelText("Solving..."));
    }

    @Override
    public void finishedSolving(INoriGame noriGame, boolean stepping) {
        finishedSolving(noriGame, stepping, false);
    }

    @Override
    public void finishedSolving(INoriGame noriGame, boolean stepping, boolean steppingResult) {
        String stateText = "Solved";
        if (noriGame.isSolved()) stateText = "Solved successfully";
        else if (!stepping) {
            stateText = "Solving failed";
            Platform.runLater(() -> controller.alertController.showNotSolvableAlert());
        } else if (steppingResult) stateText = "Stepped";
        else stateText = "Stepped back";

        String finalStateText = stateText;
        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
        Platform.runLater(() -> controller.setDisableButtons(false));
        Platform.runLater(() -> controller.setStateLabelText(finalStateText));
    }
}
