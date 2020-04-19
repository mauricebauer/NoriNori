package norinori.controller;

import norinori.model.NoriGame;
import norinori.model.Solver;
import norinori.model.UiHelper;

import java.nio.file.Files;
import java.nio.file.Path;

public class GameController {
    private final MainWindowController controller;
    private final Solver solver = new Solver();
    public boolean isSolverRunning = false;
    private NoriGame noriGame;

    /**
     * Constructor of the GameController class
     * @param mainWindowController Reference to the main controller (for other references)
     */
    public GameController(MainWindowController mainWindowController) {
        this.controller = mainWindowController;
    }

    /**
     * Reads and sets the NoriGame from a Json file stored at the passed path
     * @param filePath Path to a valid json file containing a NoriGame
     */
    public void readGameFromFile(String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!path.toString().endsWith(".json"))
                throw new Exception("Could not load file!");

            String content = Files.readString(path);
            noriGame = new NoriGame(content);
            solver.reset();
            controller.gridController.createBoard(noriGame);
            controller.grid.setVisible(true);
            controller.dragAndDropField.setVisible(false);
            controller.sidebarController.setStats(noriGame.getMaxCol() + 1, noriGame.getMaxRow() + 1, noriGame.getMaxRegion() + 1);
            controller.sidebarController.setButtons(false, false, false);
            controller.setButtons(false, false, false, false);
        } catch (Exception e) {
            controller.grid.setVisible(false);
            controller.dragAndDropField.setVisible(true);
            controller.sidebarController.setButtons(true, true, true);
            controller.setButtons(true, true, true, false);
            controller.statisticsGrid.setVisible(false);
            controller.alertController.showFileError();
        }
        controller.grid.getScene().getWindow().sizeToScene();
    }

    /**
     * Starts the solver in a separate thread
     * @param stepping True if the solver should only solve one step
     */
    public void startSolver(boolean stepping) {
        if (isSolverRunning) return;
        Runnable solve = () -> solver.solve(noriGame, new UiHelper(controller), stepping);
        Thread solverThread = new Thread(solve, "Solver");
        solverThread.setDaemon(true);
        solverThread.start();
    }

    /**
     * Resets the NoriGame and all GUI elements for a fresh start
     */
    public void resetGame() {
        if (isSolverRunning) return;
        noriGame.resetCells();
        controller.gridController.colorCells(noriGame);
        solver.reset();
        controller.sidebarController.setButtons(false, false, false);
        controller.setButtons(false, false, false, false);
    }
}
