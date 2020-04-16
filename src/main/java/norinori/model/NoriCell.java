package norinori.model;

public class NoriCell {
    private final int col;
    private final int row;
    private final int region;
    private NoriCellState state;

    public NoriCell(int col, int row, int region) {
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
        // state is null if deserialized via gson from json
        if (state == null)
            return NoriCellState.UNMARKED;

        return state;
    }

    public void setState(NoriCellState state) {
        this.state = state;
    }
}
