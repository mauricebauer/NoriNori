package norinori.model;

import javafx.application.Platform;
import norinori.controller.MainWindowController;

public class UiHelper {
    private final MainWindowController controller;

    /**
     * Constructor of the UiHelper class
     * @param controller Reference to the GUI controller (needed for the GUI elements)
     */
    public UiHelper(MainWindowController controller) {
        this.controller = controller;
    }

    /**
     * Triggers a rerender of the GUI board
     * @param noriGame The NoriGame where the cell states should be retrieved from
     */
    public void updateCellColor(NoriGame noriGame) {
        Platform.runLater(() -> controller.gridController.colorCells(noriGame));
    }

    /**
     * Disable buttons which must not be pressed while solving
     */
    public void startedSolving() {
        Platform.runLater(() -> controller.sidebarController.setButtons(true, true, true));
        Platform.runLater(() -> controller.setButtons(false, false, true, true));
    }

    /**
     * Recover buttons which were disabled while solving. Also show alert if game cannot be solved.
     * @param noriGame The game which is finished solving
     * @param stepping Was the current result solved using stepping or through running through? True if stepped.
     */
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
