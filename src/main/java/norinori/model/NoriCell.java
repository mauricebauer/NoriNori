package norinori.model;

import java.security.InvalidParameterException;

public class NoriCell {
    private final int col;
    private final int row;
    private final int region;
    private NoriCellState state;

    /**
     * Constructor for creating a new NoriCell
     * @param col Column (X) of the cell (zero based)
     * @param row Row (Y) of the cell (zero based)
     * @param region Region identifier (must be positive and equal across same region)
     * @throws InvalidParameterException
     */
    public NoriCell(int col, int row, int region) {
        if (col < 0 || row < 0 || region < 0)
            throw new InvalidParameterException("Parameters must be positive");

        this.col = col;
        this.row = row;
        this.region = region;
    }

    /**
     * Copy constructor to clone a NoriCell
     * @param originalCell Original cell which fields should be copied
     */
    public NoriCell(NoriCell originalCell) {
        this.col = originalCell.getCol();
        this.row = originalCell.getRow();
        this.region = originalCell.getRegion();
        this.state = originalCell.getState();
    }

    /**
     *
     * @return Column (X) of the cell (zero based)
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @return Row (Y) of the cell (zero based)
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return Region identifier (is positive and equal across same region)
     */
    public int getRegion() {
        return region;
    }

    /**
     *
     * @return NoriCellState of the cell (if null then UNMARKED is returned)
     */
    public NoriCellState getState() {
        // state can be null if cell is created by Gson from JSON
        return state != null ? state : NoriCellState.UNMARKED;
    }

    /**
     *
     * @param state State of the cell
     */
    public void setState(NoriCellState state) {
        this.state = state;
    }
}
