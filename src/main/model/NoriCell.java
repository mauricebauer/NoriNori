package main.model;

public class NoriCell {
    private final int col;
    private final int row;
    private final int regionId;
    private NoriCellState state;

    public NoriCell(int col, int row, int regionId) {
        this.col = col;
        this.row = row;
        this.regionId = regionId;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getRegionId() {
        return regionId;
    }

    public NoriCellState getState() {
        if (state == null)
            return NoriCellState.UNMARKED;

        return state;
    }

    public void setState(NoriCellState state) {
        this.state = state;
    }
}