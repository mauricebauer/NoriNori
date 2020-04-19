package norinori.model;

import java.util.List;

public interface INoriGame {
    NoriCell getCell(int col, int row);

    int getMaxCol();

    int getMaxRow();

    int getMaxRegion();

    boolean checkStateAtCell(NoriCell cell, NoriCellState stateToCheck);

    boolean isSolved();

    void resetCells();

    List<NoriCell> getNoriCells();
}
