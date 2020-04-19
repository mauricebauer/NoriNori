package norinori.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class NoriRegion {
    // A list is efficient enough because this will always get fully iterated
    public final List<NoriCell> cellList = new ArrayList<>();

    public void addCell(NoriCell cell) {
        if (cellList.size() > 0 && cell.getRegion() != cellList.get(0).getRegion())
            throw new InvalidParameterException("Only cells of one region are allowed");

        cellList.add(cell);
    }

    // Returns true if placement is valid
    public boolean checkIfPlacementIsValid(NoriCellState state) {
        int numberOfUnmarkedCells = getNumberOfCells(NoriCellState.UNMARKED);
        int numberOfBlackCells = getNumberOfCells(NoriCellState.BLACK);

        // Ensure that there is always enough room for 2 black cells
        if (state != NoriCellState.BLACK && numberOfUnmarkedCells <= (2 - numberOfBlackCells))
            return false;

        // Ensure that there are not more than 2 black cells
        return state != NoriCellState.BLACK || numberOfBlackCells < 2;
    }

    public int getNumberOfCells(NoriCellState stateToCount) {
        int count = 0;
        // for-loop is faster than a stream and this here is quite easy
        for (NoriCell cell : cellList) {
            if (cell.getState() == stateToCount) count++;
        }
        return count;
    }
}
