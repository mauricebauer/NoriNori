package norinori.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

// This solver uses backtracking
public class Solver {
    public static final int uiUpdateFrequency = 50000;  // how many loops should be iterated until next ui refresh
    // The cells added to the stack are clones of the original ones
    // (to store the move - do not replace the original cell with the clone -> only the values)
    private final Deque<NoriCell> stack = new ArrayDeque<>();

    public void solve(INoriGame noriGame, IUiHelper uiHelper, boolean stepping) {
        if (noriGame.isSolved()) {
            uiHelper.finishedSolving(noriGame, stepping);
            return;
        }

        uiHelper.startedSolving();
        try {
            NoriCell cellToSolve = getNextCell(noriGame);

            if (stepping) {
                if (cellToSolve != null)
                    solveStep(noriGame, cellToSolve);
                uiHelper.finishedSolving(noriGame, true);
                return;
            }

            int uiUpdateCounter = 0;
            while (cellToSolve != null) {
                solveStep(noriGame, cellToSolve);
                cellToSolve = getNextCell(noriGame);
                if (++uiUpdateCounter > uiUpdateFrequency) {
                    uiHelper.updateCellColor(noriGame);
                    uiUpdateCounter = 0;
                }
            }
            uiHelper.finishedSolving(noriGame, false);
        } catch (NoSuchElementException e) {
            // Exception will occur if stack is empty and tried to pop()
            // This occurs if the board is not valid / solvable
            uiHelper.finishedSolving(noriGame, stepping);
        }
    }

    private void solveStep(INoriGame noriGame, NoriCell cellToSolve) {
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

    public void reset() {
        stack.clear();
    }

    private NoriCell getNextCell(INoriGame noriGame) {
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
