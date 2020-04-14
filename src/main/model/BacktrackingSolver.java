package main.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BacktrackingSolver implements ISolver {
    private final List<NoriCellState> possibleStates = new ArrayList<>() {{
        add(NoriCellState.MARKED_BLACK);
        add(NoriCellState.MARKED_WHITE);
    }};
    private final Deque<NoriCell> stack = new ArrayDeque<>();

    @Override
    public boolean solve(NoriGame noriGame, boolean doOnlyOneStep) {
        // printBoard(noriGame);
        NoriCell unmarkedCell = noriGame.findUnmarkedCell();
        if (unmarkedCell == null)
            return true;  // Finished solving if no unmarked cell left

        for (NoriCellState state : possibleStates) {
            if (noriGame.checkIfPossible(unmarkedCell.getCol(), unmarkedCell.getRow(), state)) {
                unmarkedCell.setState(state);
                if (solve(noriGame, doOnlyOneStep)) return true;
                else unmarkedCell.setState(NoriCellState.UNMARKED);
            }
        }

        return false;  // No solution found
    }

    private void printBoard(NoriGame noriGame) {
        System.out.println();
        System.out.println();
        for (int row = 0; row <= noriGame.getMaxRow(); row++) {
            for (int col = 0; col <= noriGame.getMaxColumn(); col++) {
                switch (noriGame.getCell(col, row).getState()) {
                    case UNMARKED:
                        System.out.print("- ");
                        break;
                    case MARKED_WHITE:
                        System.out.print("0 ");
                        break;
                    case MARKED_BLACK:
                        System.out.print("X ");
                        break;
                }
            }
            System.out.println();
        }
    }

    @Override
    public void reset() {
    }
}