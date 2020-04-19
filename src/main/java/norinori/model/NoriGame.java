package norinori.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class NoriGame {
    // Indexes are always ((maxCol+1) * row) + col
    private final List<NoriCell> noriCells = new ArrayList<>();
    private final List<NoriRegion> noriRegions = new ArrayList<>();
    private final int maxRow, maxCol;

    /**
     * Constructor for NoriGame which creates a NoriNori Board from a JSON string
     * @param jsonString Valid JSON string (e.g. "[[0,0],[1,1]]" for a 2x2 board with 2 regions)
     * @throws Exception Thrown if Json string or Json board is not valid
     */
    public NoriGame(String jsonString) {
        // Parse JSON
        Type jsonType = new TypeToken<ArrayList<ArrayList<Integer>>>() {
        }.getType();
        ArrayList<ArrayList<Integer>> input = new Gson().fromJson(jsonString, jsonType);

        // Calculate sizes
        int rows = input.size();
        int columns = input.get(0).size();
        maxRow = rows - 1;
        maxCol = columns - 1;

        // Initialize regions
        HashSet<Integer> regionSet = new HashSet<>();
        for (ArrayList<Integer> row : input)
            regionSet.addAll(row);
        for (int i = 0; i <= Collections.max(regionSet); i++)
            getNoriRegions().add(new NoriRegion());

        // Initialize cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                NoriCell cell = new NoriCell(col, row, input.get(row).get(col));
                getNoriCells().add(cell);  // Be careful with the position of the object in the list!
                getNoriRegions().get(cell.getRegion()).addCell(cell);
            }
        }
    }

    /**
     * Check if the board is solved (board is considered solved if there is no cell UNMARKED anymore)
     * @return True if the game is solved
     */
    public boolean isSolved() {
        return findUnmarkedCell() == null;
    }

    /**
     * Marks all cells of the game as UNMARKED
     */
    public void resetCells() {
        for (NoriCell cell : getNoriCells()) {
            cell.setState(NoriCellState.UNMARKED);
        }
    }

    /**
     * Checks if the passed state is allowed to be applied to the passed cell
     * @param cell Cell at which the passed state should be validated
     * @param stateToCheck Future state of the cell which should be validated
     * @return True if the passed state can be applied to the passed cell and the board is still valid
     * @throws InvalidParameterException Thrown if state to check is UNMARKED
     */
    public boolean checkStateAtCell(NoriCell cell, NoriCellState stateToCheck) {
        // Check if state to check is valid
        if (stateToCheck == NoriCellState.UNMARKED)
            throw new InvalidParameterException("Check of UNMARKED state not allowed!");

        // Check region
        NoriRegion regionOfCell = getNoriRegions().get(cell.getRegion());
        if (!regionOfCell.checkIfPlacementIsValid(stateToCheck))
            return false;

        // Check black placement
        if (stateToCheck == NoriCellState.BLACK) {
            if (isDominoAroundCell(cell))
                return false;

            if (isMoreThanOneLonelyBlackCellAround(cell))
                return false;
        }

        // Check white placement
        return stateToCheck != NoriCellState.WHITE || !willThisPlacementEncapsulateALonelyBlackCell(cell);
    }

    /**
     * Checks if there is a domino (two BLACK cells together) around the passed cell
     * @param cell Cell which should be checked for a near domino
     * @return True if a domino is near the passed cell thus the placement is not valid
     */
    private boolean isDominoAroundCell(NoriCell cell) {
        // Check top, then right, then bottom, then left
        return (cell.getRow() > 0 && isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() - 1))) ||
                (cell.getCol() < getMaxCol() && isCellPartOfDomino(getCell(cell.getCol() + 1, cell.getRow()))) ||
                (cell.getRow() < getMaxRow() && isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() + 1))) ||
                (cell.getCol() > 0 && isCellPartOfDomino(getCell(cell.getCol() - 1, cell.getRow())));
    }

    /**
     * Checks if the passed cell is part of a domino (two BLACK cells together)
     * @param cell Cell which should be checked
     * @return True if the cell is part of a domino
     */
    private boolean isCellPartOfDomino(NoriCell cell) {
        if (cell.getState() != NoriCellState.BLACK) return false;

        // Check top, then right, then bottom, then left
        return (cell.getRow() > 0 && getCell(cell.getCol(), cell.getRow() - 1).getState() == NoriCellState.BLACK) ||
                (cell.getCol() < getMaxCol() && getCell(cell.getCol() + 1, cell.getRow()).getState() == NoriCellState.BLACK) ||
                (cell.getRow() < getMaxRow() && getCell(cell.getCol(), cell.getRow() + 1).getState() == NoriCellState.BLACK) ||
                (cell.getCol() > 0 && getCell(cell.getCol() - 1, cell.getRow()).getState() == NoriCellState.BLACK);
    }

    /**
     * Checks if there is > 1 lonely (not part of a domino) BLACK cells around
     * @param cell Cell which position should be checked
     * @return True if there is > 1 lonely BLACK cells around
     */
    public boolean isMoreThanOneLonelyBlackCellAround(NoriCell cell) {
        int count = 0;

        // Check if top is lonely black cell
        if (cell.getRow() > 0 && getCell(cell.getCol(), cell.getRow() - 1).getState() == NoriCellState.BLACK &&
                !isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() - 1)))
            count++;

        // Check if right is lonely black cell
        if (cell.getCol() < getMaxCol() && getCell(cell.getCol() + 1, cell.getRow()).getState() == NoriCellState.BLACK &&
                !isCellPartOfDomino(getCell(cell.getCol() + 1, cell.getRow())))
            count++;

        // Check if bottom is lonely black cell
        if (cell.getRow() < getMaxRow() && getCell(cell.getCol(), cell.getRow() + 1).getState() == NoriCellState.BLACK &&
                !isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() + 1)))
            count++;

        // Check if left is lonely black cell
        if (cell.getCol() > 0 && getCell(cell.getCol() - 1, cell.getRow()).getState() == NoriCellState.BLACK &&
                !isCellPartOfDomino(getCell(cell.getCol() - 1, cell.getRow())))
            count++;

        return count > 1;
    }

    /**
     * Check if a WHITE placement at the passed cell encapsulates a lonely black cell around
     * @param cell Cell where the WHITE placement should be checked
     * @return True if a lonely BLACK cell will be encapsulated and thus the placement of a WHITE cell is not valid
     */
    public boolean willThisPlacementEncapsulateALonelyBlackCell(NoriCell cell) {
        // Check top
        if (cell.getRow() > 0 && getCell(cell.getCol(), cell.getRow() - 1).getState() == NoriCellState.BLACK &&
                willThisPlacementEncapsulateTheGivenCell(cell.getCol(), cell.getRow() - 1, NoriDominoDirection.BOTTOM))
            return true;

        // Check right
        if (cell.getCol() < getMaxCol() && getCell(cell.getCol() + 1, cell.getRow()).getState() == NoriCellState.BLACK &&
                willThisPlacementEncapsulateTheGivenCell(cell.getCol() + 1, cell.getRow(), NoriDominoDirection.LEFT))
            return true;

        // Check bottom
        if (cell.getRow() < getMaxRow() && getCell(cell.getCol(), cell.getRow() + 1).getState() == NoriCellState.BLACK &&
                willThisPlacementEncapsulateTheGivenCell(cell.getCol(), cell.getRow() + 1, NoriDominoDirection.TOP))
            return true;

        // Check left
        return cell.getCol() > 0 && getCell(cell.getCol() - 1, cell.getRow()).getState() == NoriCellState.BLACK &&
                willThisPlacementEncapsulateTheGivenCell(cell.getCol() - 1, cell.getRow(), NoriDominoDirection.RIGHT);
    }

    /**
     * Checks if a not BLACK placement would encapsulate the cell positioned at the parameters
     * @param col Column of the BLACK cell which should be checked for encapsulation
     * @param row Row of the BLACK cell which should be checked for encapsulation
     * @param whereWillTheWhiteCellBePlaced Where is the cell which is being checked for a WHITE placement (from the row and column sight)
     * @return True if the BLACK cell (at col and row) will be encapsulated by the cell from the passed direction
     */
    public boolean willThisPlacementEncapsulateTheGivenCell(int col, int row, NoriDominoDirection whereWillTheWhiteCellBePlaced) {
        // Check top
        if (row > 0 && getCell(col, row - 1).getState() != NoriCellState.WHITE &&
                whereWillTheWhiteCellBePlaced != NoriDominoDirection.TOP)
            return false;

        // Check right
        if (col < getMaxCol() && getCell(col + 1, row).getState() != NoriCellState.WHITE &&
                whereWillTheWhiteCellBePlaced != NoriDominoDirection.RIGHT)
            return false;

        // Check bottom
        if (row < getMaxRow() && getCell(col, row + 1).getState() != NoriCellState.WHITE &&
                whereWillTheWhiteCellBePlaced != NoriDominoDirection.BOTTOM)
            return false;

        // Check left
        return col <= 0 || getCell(col - 1, row).getState() == NoriCellState.WHITE ||
                whereWillTheWhiteCellBePlaced == NoriDominoDirection.LEFT;
    }

    /**
     * Finds the first UNMARKED cell in the cellList
     * @return The first UNMARKED cell (null if no UNMARKED cell found)
     */
    public NoriCell findUnmarkedCell() {
        for (NoriCell cell : getNoriCells()) {
            if (cell.getState() == NoriCellState.UNMARKED) return cell;
        }
        return null;
    }

    /**
     *
     * @return The original list of NoriCells (no copy)
     */
    public List<NoriCell> getNoriCells() {
        return noriCells;
    }

    /**
     *
     * @return The original list of NoriRegions (no copy)
     */
    public List<NoriRegion> getNoriRegions() {
        return noriRegions;
    }

    /**
     * Return the original cell at the passed place of the board
     * @param col Column (X) of the cell which should be returned (zero based)
     * @param row Row (Y) of the cell which should be returned (zero based)
     * @return The original cell at the position (col and row)
     */
    public NoriCell getCell(int col, int row) {
        return getNoriCells().get((maxCol + 1) * row + col);
    }

    /**
     *
     * @return The max row number (with 6 rows, getMaxRow() returns 5)
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     *
     * @return The max column number (with 6 columns, getMaxCol() returns 5)
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     *
     * @return The max region identifier number
     */
    public int getMaxRegion() {
        return getNoriRegions().size() - 1;
    }
}
