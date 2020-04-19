package norinori.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

// This solver uses backtracking
public class Solver {
    // The cells added to the stack are clones of the original ones
    // (to store the move - do not replace the original cell with the clone -> only the values)
    public final Deque<NoriCell> stack = new ArrayDeque<>();
    public int uiUpdateFrequency = 50000;  // how many loops should be iterated until next ui refresh

    /**
     * Solve the passed NoriGame and store the result in that game
     * @param noriGame The game which have to solved and the result stored in
     * @param uiHelper UiHelper class used to update the GUI (from Solver thread)
     * @param stepping True if only one backtracking step should be performed. False if game should be solved completely.
     */
    public void solve(NoriGame noriGame, UiHelper uiHelper, boolean stepping) {
        if (noriGame.isSolved()) {
            if (uiHelper != null) uiHelper.finishedSolving(noriGame, stepping);
            return;
        }

        if (uiHelper != null) uiHelper.startedSolving();
        try {
            NoriCell cellToSolve = getNextCell(noriGame);

            if (stepping) {
                if (cellToSolve != null)
                    solveStep(noriGame, cellToSolve);
                if (uiHelper != null) uiHelper.finishedSolving(noriGame, true);
                return;
            }

            int uiUpdateCounter = 0;
            while (cellToSolve != null) {
                solveStep(noriGame, cellToSolve);
                cellToSolve = getNextCell(noriGame);
                if (++uiUpdateCounter > uiUpdateFrequency) {
                    if (uiHelper != null) uiHelper.updateCellColor(noriGame);
                    uiUpdateCounter = 0;
                }
            }
            if (uiHelper != null) uiHelper.finishedSolving(noriGame, false);
        } catch (NoSuchElementException e) {
            // Exception will occur if stack is empty and tried to pop()
            // This occurs if the board is not valid / solvable
            if (uiHelper != null) uiHelper.finishedSolving(noriGame, stepping);
        }
    }

    /**
     * Solve one step of the passed game at the passed cell
     * @param noriGame The game to solve and store the result to
     * @param cellToSolve The cell where the state should be found
     */
    private void solveStep(NoriGame noriGame, NoriCell cellToSolve) {
        if (cellToSolve.getState() == NoriCellState.UNMARKED &&
                noriGame.checkStateAtCell(cellToSolve, NoriCellState.BLACK)) {
            cellToSolve.setState(NoriCellState.BLACK);
            stack.push(new NoriCell(cellToSolve));  // Store move as clone on stack
            return;
        }

        if (cellToSolve.getState() == NoriCellState.UNMARKED || cellToSolve.getState() == NoriCellState.BLACK) {
            cellToSolve.setState(NoriCellState.UNMARKED);
            if (noriGame.checkStateAtCell(cellToSolve, NoriCellState.WHITE)) {
                cellToSolve.setState(NoriCellState.WHITE);
                stack.push(new NoriCell(cellToSolve));  // Store move as clone on stack
                return;
            }
        }

        // Every other case where there is no more possibility -> backtrack
        cellToSolve.setState(NoriCellState.UNMARKED);
        stack.pop();  // Can throw an exception but it is handled in solve()
    }

    /**
     * Reset the current state of the solver to start a new solving
     */
    public void reset() {
        stack.clear();
    }

    /**
     * Get the next cell which has to be solved. Goes from top left to bottom right line by line.
     * @param noriGame The game which should be solved
     * @return The original cell which should be solved by the next solver step
     */
    private NoriCell getNextCell(NoriGame noriGame) {
        if (stack.isEmpty()) return noriGame.getCell(0, 0);

        NoriCell cellOnStack = stack.peek();
        // Next cell is on the same row
        if (cellOnStack.getCol() < noriGame.getMaxCol())
            return noriGame.getCell(cellOnStack.getCol() + 1, cellOnStack.getRow());

        // Next cell is on next row
        if (cellOnStack.getRow() < noriGame.getMaxRow())
            return noriGame.getCell(0, cellOnStack.getRow() + 1);

        return null;  // End reached
    }
}
