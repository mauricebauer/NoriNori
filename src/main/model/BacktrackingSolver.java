package main.model;

import java.util.*;

public class BacktrackingSolver implements ISolver {
    private final List<NoriCellState> possibleStates = new ArrayList<>() {{
        add(NoriCellState.BLACK);
        add(NoriCellState.WHITE);
    }};
    // The cells added to the stack are clones of the original ones
    // (to store the move - never replace the original cell with the clone -> only the values)
    private final Deque<NoriCell> stack = new ArrayDeque<>();

    @Override
    public boolean solve(NoriGame noriGame, boolean doOnlyOneStep) {
        try {
            if (doOnlyOneStep && noriGame.findUnmarkedCell() != null) {
                return solveStep(noriGame);
            } else if (!doOnlyOneStep) {
                boolean result = true;
                while (noriGame.findUnmarkedCell() != null) {
                    result = solveStep(noriGame);
                }
                return result;
            }
            return true;
        } catch (NoSuchElementException e) {
            // Exception will occur if stack is empty and tried to pop()
            // This occurs if the gameboard is not valid / solvable
            return false;
        }
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

    @Override
    public void reset() {
        stack.clear();
    }

    private NoriCell getNextCell(NoriGame noriGame, NoriCell cell) {
        if (cell.getCol() < noriGame.getMaxCol())
            return noriGame.getCell(cell.getCol() + 1, cell.getRow());

        return noriGame.getCell(0, cell.getRow() + 1);
    }
}
