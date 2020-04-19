package norinori.model;

import java.security.InvalidParameterException;

public class NoriCell {
    private final int col;
    private final int row;
    private final int region;
    private NoriCellState state;

    public NoriCell(int col, int row, int region) {
        if (col < 0 || row < 0 || region < 0)
            throw new InvalidParameterException("Parameters must be positive");

        this.col = col;
        this.row = row;
        this.region = region;
    }

    // Copy constructor
    public NoriCell(NoriCell originalCell) {
        this.col = originalCell.getCol();
        this.row = originalCell.getRow();
        this.region = originalCell.getRegion();
        this.state = originalCell.getState();
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getRegion() {
        return region;
    }

    public NoriCellState getState() {
        // state can be null if cell is created by Gson from JSON
        return state != null ? state : NoriCellState.UNMARKED;
    }

    public void setState(NoriCellState state) {
        this.state = state;
    }
}
