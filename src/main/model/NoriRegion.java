package main.model;

import java.util.ArrayList;
import java.util.List;

public class NoriRegion {
    // A list is sufficient because this will always get fully iterated
    private final List<NoriCell> cellList = new ArrayList<>();

    public void addCell(NoriCell cell) {
        cellList.add(cell);
    }

    // Returns true if placement is valid
    public boolean checkIfPlacementInRegionIsValid(NoriCellState state) {
        int numberOfUnmarkedCells = getNumberOfCells(NoriCellState.UNMARKED);
        int numberOfBlackCells = getNumberOfCells(NoriCellState.BLACK);

        // Ensure that there is always enough room for 2 black cells
        if (state == NoriCellState.WHITE && numberOfUnmarkedCells <= (2 - numberOfBlackCells))
            return false;

        // Ensure that there are not more than 2 black cells
        return state != NoriCellState.BLACK || numberOfBlackCells < 2;
    }

    private int getNumberOfCells(NoriCellState stateToCount) {
        int count = 0;
        // Forloop is faster than a stream and this here is quite easy
        for (NoriCell cell : cellList) {
            if (cell.getState() == stateToCount) count++;
        }
        return count;
    }
}
