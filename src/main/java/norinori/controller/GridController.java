package norinori.controller;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import norinori.model.NoriCell;
import norinori.model.NoriGame;

public class GridController {
    public static final double SIZE_OF_CELL = 40;
    public static final double BORDER_THICKNESS_NORMAL = 0.5;
    public static final double BORDER_THICKNESS_THICK = 2;

    private final GridPane grid;
    private double currentSizeOfCell;

    /**
     * Constructor for the GridController. It manages the grid element which is showing the NoriGame
     * @param grid The GUI grid node where the NoriGame should be rendered
     */
    public GridController(GridPane grid) {
        if (grid == null)
            throw new NullPointerException("Null grid not allowed!");

        this.grid = grid;
        currentSizeOfCell = SIZE_OF_CELL;
    }

    /**
     * Increases or decreases the width and height of the cells (also directly in the GUI)
     * @param shouldBeMadeBigger True if the cells should be made bigger. False if should be made smaller.
     */
    public void resizeBoard(boolean shouldBeMadeBigger) {
        currentSizeOfCell = shouldBeMadeBigger ? currentSizeOfCell * 1.1 : currentSizeOfCell / 1.1;

        for (RowConstraints row : grid.getRowConstraints())
            row.setPrefHeight(currentSizeOfCell);
        for (ColumnConstraints column : grid.getColumnConstraints())
            column.setPrefWidth(currentSizeOfCell);
    }

    /**
     * Rerender all cell colors according to the states in the passed NoriGame.
     * @param noriGame The game where the cell states should be retrieved from
     */
    public void colorCells(NoriGame noriGame) {
        for (Node node : grid.getChildren()) {
            NoriCell cell = noriGame.getCell(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            switch (cell.getState()) {
                case UNMARKED:
                    node.setStyle("-fx-background-color: #DEE2E6;");
                    break;
                case WHITE:
                    node.setStyle("-fx-background-color: white;");
                    break;
                case BLACK:
                    node.setStyle("-fx-background-color: #868E96;");
                    break;
            }
        }
    }

    /**
     * Instantiate the GUI board with the content of the passed NoriGame
     * @param noriGame The game which should be rendered in the GUI grid node
     */
    public void createBoard(NoriGame noriGame) {
        int rows = noriGame.getMaxRow() + 1;  // Because getMaxRow() is the index, the count is always + 1
        int columns = noriGame.getMaxCol() + 1;  // Because getMaxCol() is the index, the count is always + 1

        // Reset GridPanel
        grid.setSnapToPixel(false);  // Needed for clearer and sharper edges in GUI
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

        // Create rows and columns with fixed size
        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(currentSizeOfCell);
            grid.getRowConstraints().add(row);
        }
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(currentSizeOfCell);
            grid.getColumnConstraints().add(column);
        }

        // Create cells with correct borders
        for (NoriCell noriCell : noriGame.getNoriCells()) {
            double topBorder, rightBorder, botBorder, leftBorder;
            topBorder = rightBorder = botBorder = leftBorder = BORDER_THICKNESS_NORMAL;

            int curCol = noriCell.getCol();
            int curRow = noriCell.getRow();
            int curRegionId = noriCell.getRegion();

            // Check top border
            if (curRow <= 0 || noriGame.getCell(curCol, curRow - 1).getRegion() != curRegionId) {
                topBorder = BORDER_THICKNESS_THICK;
            }
            // Check right border
            if (curCol >= columns - 1 || noriGame.getCell(curCol + 1, curRow).getRegion() != curRegionId) {
                rightBorder = BORDER_THICKNESS_THICK;
            }
            // Check bottom border
            if (curRow >= rows - 1 || noriGame.getCell(curCol, curRow + 1).getRegion() != curRegionId) {
                botBorder = BORDER_THICKNESS_THICK;
            }
            // Check left border
            if (curCol <= 0 || noriGame.getCell(curCol - 1, curRow).getRegion() != curRegionId) {
                leftBorder = BORDER_THICKNESS_THICK;
            }

            Pane guiCell = new Pane();
            drawBorder(guiCell, topBorder, rightBorder, botBorder, leftBorder);
            grid.add(guiCell, curCol, curRow);
        }

        colorCells(noriGame);
    }

    /**
     * Draws a border around the passed GUI cell node with the passed widths
     * @param cell Cell which should get new borders
     * @param top Width of the border at the top
     * @param right Width of the border at the right
     * @param bottom Width of the border at the bottom
     * @param left Width of the border at the left
     */
    private void drawBorder(Pane cell, double top, double right, double bottom, double left) {
        cell.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(top, right, bottom, left))));
    }
}
