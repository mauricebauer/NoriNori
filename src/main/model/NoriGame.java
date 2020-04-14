package main.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoriGame {
    private List<NoriCell> noriCellList = new ArrayList<>();
    private int maxRow = -1, maxCol = -1;

    public NoriGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                noriCellList.add(new NoriCell(i, j, 0));
            }
        }
    }

    public NoriGame(String jsonString) {
        Type jsonType = new TypeToken<ArrayList<ArrayList<Integer>>>() {
        }.getType();
        ArrayList<ArrayList<Integer>> input = new Gson().fromJson(jsonString, jsonType);
        noriCellList.clear();
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).size(); col++) {
                noriCellList.add(new NoriCell(col, row, input.get(row).get(col)));
            }
        }
    }

    public void resetCells() {
        for (NoriCell cell : noriCellList) {
            cell.setState(NoriCellState.UNMARKED);
        }
    }

    public List<NoriCell> getNoriCellList() {
        return noriCellList;
    }

    public void setNoriCellList(List<NoriCell> noriCellList) {
        this.noriCellList = noriCellList;
    }

    public NoriCell getCell(int col, int row) {
        return noriCellList.stream().filter(c -> c.getCol() == col && c.getRow() == row).findFirst().get();
    }

    public int getMaxRow() {
        if (maxRow == -1) {
            maxRow = noriCellList.stream().map(NoriCell::getRow).max(Integer::compareTo).get();
        }
        return maxRow;
    }

    public int getMaxColumn() {
        if (maxCol == -1) {
            maxCol = noriCellList.stream().map(NoriCell::getCol).max(Integer::compareTo).get();
        }
        return maxCol;
    }

    // Returns null if no Cell found
    public NoriCell findUnmarkedCell() {
        for (NoriCell cell : noriCellList) {
            if (cell.getState() == NoriCellState.UNMARKED)
                return cell;
        }
        return null;
    }

    public boolean checkIfPossible(int col, int row, NoriCellState state) {
        NoriCell cellAtPosition = getCell(col, row);
        if (state == NoriCellState.UNMARKED || cellAtPosition.getState() != NoriCellState.UNMARKED) {
            System.out.println("Tried to check unmarked (as new state) or a place which is already marked!");
            return false;
        }

        // Check the current region
        int curRegionId = cellAtPosition.getRegionId();
        List<NoriCell> cellsThisRegion = noriCellList.stream().filter(c -> c.getRegionId() == curRegionId).collect(Collectors.toList());
        long nUnmarkedCells = cellsThisRegion.stream().filter(c -> c.getState() == NoriCellState.UNMARKED).count();
        long nBlackCells = cellsThisRegion.stream().filter(c -> c.getState() == NoriCellState.MARKED_BLACK).count();

        // Ensure that exactly two black cells are in each region
        if (state == NoriCellState.MARKED_WHITE) {
            if (nUnmarkedCells <= (2 - nBlackCells)) return false;
        } else {
            if (nBlackCells >= 2) return false;
        }

        // Ensure that no dominos are positioned together
        if (state == NoriCellState.MARKED_BLACK) {
            int numberOfLonelyNeighbours = 0;
            // Check top neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.TOP)) {
                if (hasNeighbourInDirection(col, row - 1, NoriDominoDirection.TOP) ||
                        hasNeighbourInDirection(col, row - 1, NoriDominoDirection.LEFT) ||
                        hasNeighbourInDirection(col, row - 1, NoriDominoDirection.RIGHT))
                    return false;
                else numberOfLonelyNeighbours += 1;
            }
            // Check right neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.RIGHT)) {
                if (hasNeighbourInDirection(col + 1, row, NoriDominoDirection.TOP) ||
                        hasNeighbourInDirection(col + 1, row, NoriDominoDirection.RIGHT) ||
                        hasNeighbourInDirection(col + 1, row, NoriDominoDirection.BOTTOM))
                    return false;
                else numberOfLonelyNeighbours += 1;
            }
            // Check bottom neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.BOTTOM)) {
                if (hasNeighbourInDirection(col, row + 1, NoriDominoDirection.BOTTOM) ||
                        hasNeighbourInDirection(col, row + 1, NoriDominoDirection.LEFT) ||
                        hasNeighbourInDirection(col, row + 1, NoriDominoDirection.RIGHT))
                    return false;
                else numberOfLonelyNeighbours += 1;
            }
            // Check left neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.LEFT)) {
                if (hasNeighbourInDirection(col - 1, row, NoriDominoDirection.TOP) ||
                        hasNeighbourInDirection(col - 1, row, NoriDominoDirection.LEFT) ||
                        hasNeighbourInDirection(col - 1, row, NoriDominoDirection.BOTTOM))
                    return false;
                else numberOfLonelyNeighbours += 1;
            }

            if (numberOfLonelyNeighbours > 1)
                return false;
        }

        // Ensure that white cell does not create a lonely black cell (with no domino partner)
        if (state == NoriCellState.MARKED_WHITE) {
            // Check top neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.TOP)) {
                if (isSingleBlackCellEncapsulated(col, row - 1, NoriDominoDirection.BOTTOM)) {
                    return false;
                }
            }
            // Check right neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.RIGHT)) {
                if (isSingleBlackCellEncapsulated(col + 1, row, NoriDominoDirection.LEFT)) {
                    return false;
                }
            }
            // Check bottom neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.BOTTOM)) {
                if (isSingleBlackCellEncapsulated(col, row + 1, NoriDominoDirection.TOP)) {
                    return false;
                }
            }
            // Check left neighbour
            if (hasNeighbourInDirection(col, row, NoriDominoDirection.LEFT)) {
                return !isSingleBlackCellEncapsulated(col - 1, row, NoriDominoDirection.RIGHT);
            }
        }

        return true;
    }

    // Check if it has a domino direction first! Then do not check this here!
    private boolean isSingleBlackCellEncapsulated(int col, int row, NoriDominoDirection whereIsTheCellToPlace) {
        // Check top
        if (row > 0 && getCell(col, row - 1).getState() != NoriCellState.MARKED_WHITE && whereIsTheCellToPlace != NoriDominoDirection.TOP)
            return false;
        // Check right
        if (col < getMaxColumn() && getCell(col + 1, row).getState() != NoriCellState.MARKED_WHITE && whereIsTheCellToPlace != NoriDominoDirection.RIGHT)
            return false;
        // Check bottom
        if (row < getMaxRow() && getCell(col, row + 1).getState() != NoriCellState.MARKED_WHITE && whereIsTheCellToPlace != NoriDominoDirection.BOTTOM)
            return false;
        // Check left
        return col <= 0 || getCell(col - 1, row).getState() == NoriCellState.MARKED_WHITE || whereIsTheCellToPlace == NoriDominoDirection.LEFT;
    }

    private boolean hasNeighbourInDirection(int col, int row, NoriDominoDirection direction) {
        // Check top
        if (direction == NoriDominoDirection.TOP && row > 0 && getCell(col, row - 1).getState() == NoriCellState.MARKED_BLACK)
            return true;
        // Check right
        if (direction == NoriDominoDirection.RIGHT && col < getMaxColumn() && getCell(col + 1, row).getState() == NoriCellState.MARKED_BLACK)
            return true;
        // Check bottom
        if (direction == NoriDominoDirection.BOTTOM && row < getMaxRow() && getCell(col, row + 1).getState() == NoriCellState.MARKED_BLACK)
            return true;
        // Check left
        return direction == NoriDominoDirection.LEFT && col > 0 && getCell(col - 1, row).getState() == NoriCellState.MARKED_BLACK;
    }
}
