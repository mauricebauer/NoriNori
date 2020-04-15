package main.model;

import javafx.application.Platform;
import main.controller.MainWindowController;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class BacktrackingSolver {
    // uiUpdateFrequency is how many loops should be iterated until next ui refresh
    public static final int uiUpdateFrequency = 10000;
    // The cells added to the stack are clones of the original ones
    // (to store the move - never replace the original cell with the clone -> only the values)
    private final Deque<NoriCell> stack = new ArrayDeque<>();

    public void solve(NoriGame noriGame, boolean doOnlyOneStep, MainWindowController controller) {
        Runnable updateUiTask = () -> controller.gridController.colorCells(noriGame);
        Platform.runLater(() -> controller.setStateLabelText(doOnlyOneStep ? "Stepping..." : "Solving..."));
        try {
            if (doOnlyOneStep && noriGame.findUnmarkedCell() != null) {
                boolean result = solveStep(noriGame);
                Platform.runLater(() -> controller.setStateLabelText(result ? "Stepped" : "Stepped back"));
            } else if (!doOnlyOneStep && noriGame.findUnmarkedCell() != null) {
                int uiFrequencyCounter = 0;
                boolean result = true;
                while (noriGame.findUnmarkedCell() != null) {
                    result = solveStep(noriGame);
                    if (++uiFrequencyCounter > uiUpdateFrequency) {
                        Platform.runLater(updateUiTask);
                        uiFrequencyCounter = 0;
                    }
                }
                boolean finalResult = result;
                Platform.runLater(() -> controller.setStateLabelText(finalResult ? "Solved successful" : "Solving failed"));
                if (!finalResult) Platform.runLater(() -> controller.alertController.showNotSolvableAlert());
            } else {
                Platform.runLater(() -> controller.setStateLabelText("Solved successful"));
            }
        } catch (NoSuchElementException e) {
            // Exception will occur if stack is empty and tried to pop()
            // This occurs if the gameboard is not valid / solvable
            Platform.runLater(() -> controller.alertController.showNotSolvableAlert());
            Platform.runLater(() -> controller.setStateLabelText("Solving failed"));
        }
        Platform.runLater(updateUiTask);
        Platform.runLater(() -> controller.setDisableButtons(false));
    }

    private boolean solveStep(NoriGame noriGame) {
        NoriCell cellToSolve = stack.isEmpty() ? noriGame.getCell(0, 0) : getNextCell(noriGame, stack.peek());

        if (cellToSolve.getState() == NoriCellState.UNMARKED &&
                noriGame.checkIfPossible(cellToSolve, NoriCellState.BLACK)) {
            cellToSolve.setState(NoriCellState.BLACK);
            stack.push(new NoriCell(cellToSolve));  // Store move as clone on stack
            return true;
        }

        if (cellToSolve.getState() == NoriCellState.UNMARKED || cellToSolve.getState() == NoriCellState.BLACK) {
            cellToSolve.setState(NoriCellState.UNMARKED);
            if (noriGame.checkIfPossible(cellToSolve, NoriCellState.WHITE)) {
                cellToSolve.setState(NoriCellState.WHITE);
                stack.push(new NoriCell(cellToSolve));  // Store move as clone on stack
                return true;
            }
        }

        // Every other case where there is no more possibility -> backtrack
        cellToSolve.setState(NoriCellState.UNMARKED);
        stack.pop();  // Can throw an exception but it is handled in solve()
        return false;
    }

    public void reset() {
        stack.clear();
    }

    private NoriCell getNextCell(NoriGame noriGame, NoriCell cell) {
        if (cell.getCol() < noriGame.getMaxCol())
            return noriGame.getCell(cell.getCol() + 1, cell.getRow());

        return noriGame.getCell(0, cell.getRow() + 1);
    }
}
