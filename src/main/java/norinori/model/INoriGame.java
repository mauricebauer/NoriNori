package norinori.model;

public interface INoriGame {
    NoriCell getCell(int col, int row);

    int getMaxCol();

    int getMaxRow();

    boolean checkStateAtCell(NoriCell cell, NoriCellState stateToCheck);

    boolean isSolved();
}
