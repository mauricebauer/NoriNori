package main.model;

import java.util.ArrayList;
import java.util.List;

public class NoriGame {
    private final List<NoriCell> noriCellList = new ArrayList<>();

    public NoriGame(boolean createWithDefaultCells) {
        if (createWithDefaultCells) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    noriCellList.add(new NoriCell(i, j, 0));
                }
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

    public NoriCell getCell(int col, int row) {
        return noriCellList.stream().filter(c -> c.getCol() == col && c.getRow() == row).findFirst().get();
    }

    public int getMaxRow() {
        return noriCellList.stream().map(NoriCell::getRow).max(Integer::compareTo).get();
    }

    public int getMaxColumn() {
        return noriCellList.stream().map(NoriCell::getCol).max(Integer::compareTo).get();
    }
}
