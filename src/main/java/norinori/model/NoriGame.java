package norinori.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class NoriGame implements INoriGame {
    // Indexes are always ((maxCol+1) * row) + col
    private final List<NoriCell> noriCells = new ArrayList<>();
    private final List<NoriRegion> noriRegions = new ArrayList<>();
    private final int maxRow, maxCol;

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

    public boolean isSolved() {
        return findUnmarkedCell() == null;
    }

    // Mark all Cells as UNMARKED
    public void resetCells() {
        for (NoriCell cell : getNoriCells()) {
            cell.setState(NoriCellState.UNMARKED);
        }
    }

    public boolean checkStateAtCell(NoriCell cell, NoriCellState stateToCheck) {
        // Check if state to check is valid
        if (stateToCheck == NoriCellState.UNMARKED || cell.getState() != NoriCellState.UNMARKED)
            System.out.println("WARNING! Tried to check unmarked cell or a place which is already marked!");

        // Check region
        NoriRegion regionOfCell = getNoriRegions().get(cell.getRegion());
        if (!regionOfCell.checkIfPlacementInRegionIsValid(stateToCheck))
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

    // Returns true if there is a domino around the cell and thus the placement not valid
    private boolean isDominoAroundCell(NoriCell cell) {
        // Check top, then right, then bottom, then left
        return (cell.getRow() > 0 && isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() - 1))) ||
                (cell.getCol() < getMaxCol() && isCellPartOfDomino(getCell(cell.getCol() + 1, cell.getRow()))) ||
                (cell.getRow() < getMaxRow() && isCellPartOfDomino(getCell(cell.getCol(), cell.getRow() + 1))) ||
                (cell.getCol() > 0 && isCellPartOfDomino(getCell(cell.getCol() - 1, cell.getRow())));
    }

    // Return true if the passed cell is part of a domino
    private boolean isCellPartOfDomino(NoriCell cell) {
        if (cell.getState() != NoriCellState.BLACK) return false;

        // Check top, then right, then bottom, then left
        return (cell.getRow() > 0 && getCell(cell.getCol(), cell.getRow() - 1).getState() == NoriCellState.BLACK) ||
                (cell.getCol() < getMaxCol() && getCell(cell.getCol() + 1, cell.getRow()).getState() == NoriCellState.BLACK) ||
                (cell.getRow() < getMaxRow() && getCell(cell.getCol(), cell.getRow() + 1).getState() == NoriCellState.BLACK) ||
                (cell.getCol() > 0 && getCell(cell.getCol() - 1, cell.getRow()).getState() == NoriCellState.BLACK);
    }

    private boolean isMoreThanOneLonelyBlackCellAround(NoriCell cell) {
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

    private boolean willThisPlacementEncapsulateALonelyBlackCell(NoriCell cell) {
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

    private boolean willThisPlacementEncapsulateTheGivenCell(int col, int row, NoriDominoDirection whereWillTheWhiteCellBePlaced) {
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

    // Returns null if no Cell found
    public NoriCell findUnmarkedCell() {
        for (NoriCell cell : getNoriCells()) {
            if (cell.getState() == NoriCellState.UNMARKED) return cell;
        }
        return null;
    }

    public List<NoriCell> getNoriCells() {
        return noriCells;
    }

    public List<NoriRegion> getNoriRegions() {
        return noriRegions;
    }

    public NoriCell getCell(int col, int row) {
        return getNoriCells().get((maxCol + 1) * row + col);
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getMaxCol() {
        return maxCol;
    }

    @Override
    public int getMaxRegion() {
        return getNoriRegions().size() - 1;
    }
}
