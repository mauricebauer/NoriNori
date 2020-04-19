package norinori.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class NoriRegion {
    // A list is efficient enough because this will always get fully iterated
    public final List<NoriCell> cellList = new ArrayList<>();

    /**
     * Add the passed cell to region
     * @param cell Cell to add (must have the same region identifier as all other cells in this region)
     * @throws InvalidParameterException Thrown if parameters are not positive
     */
    public void addCell(NoriCell cell) {
        if (cellList.size() > 0 && cell.getRegion() != cellList.get(0).getRegion())
            throw new InvalidParameterException("Only cells of one region are allowed");

        cellList.add(cell);
    }

    /**
     * Checks if a placement with the passed state is valid in this region
     * Possible if there are <= 2 BLACK cells and at the end there are exactly 2 BLACK cells in this region.
     * @param state State which placement should be checked in this region
     * @return True if the state is possible to place in this region
     */
    public boolean checkIfPlacementIsValid(NoriCellState state) {
        int numberOfUnmarkedCells = getNumberOfCells(NoriCellState.UNMARKED);
        int numberOfBlackCells = getNumberOfCells(NoriCellState.BLACK);

        // Ensure that there is always enough room for 2 black cells
        if (state != NoriCellState.BLACK && numberOfUnmarkedCells <= (2 - numberOfBlackCells))
            return false;

        // Ensure that there are not more than 2 black cells
        return state != NoriCellState.BLACK || numberOfBlackCells < 2;
    }

    /**
     * Get the number of cells in this region with the passed state
     * @param stateToCount State of the cells which have to be counted
     * @return The number of cells with the passed state in the region (0 if no cell with this state found)
     */
    public int getNumberOfCells(NoriCellState stateToCount) {
        int count = 0;
        // for-loop is faster than a stream and this here is quite easy
        for (NoriCell cell : cellList) {
            if (cell.getState() == stateToCount) count++;
        }
        return count;
    }
}
