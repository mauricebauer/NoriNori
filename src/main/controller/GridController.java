package main.controller;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.model.NoriCell;
import main.model.NoriGame;

public class GridController {
    private final double SIZE_OF_CELL = 40;
    private final double BORDER_THICKNESS_NORMAL = 0.5;
    private final double BORDER_THICKNESS_THICK = 2;

    private final GridPane grid;
    private double currentSizeOfCell;
    private int rows, columns;

    public GridController(GridPane grid) {
        if (grid == null)
            throw new NullPointerException("Null grid not allowed!");

        this.grid = grid;
        currentSizeOfCell = SIZE_OF_CELL;
    }

    private void drawBorder(Pane cell, double top, double right, double bottom, double left) {
        cell.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(top, right, bottom, left))));
    }

    public void resizeBoard(boolean shouldBeMadeBigger) {
        currentSizeOfCell = shouldBeMadeBigger ? currentSizeOfCell * 1.1 : currentSizeOfCell / 1.1;

        for (RowConstraints row : grid.getRowConstraints()) {
            row.setPrefHeight(currentSizeOfCell);
        }
        for (ColumnConstraints column : grid.getColumnConstraints()) {
            column.setPrefWidth(currentSizeOfCell);
        }
    }

    public void colorCells(NoriGame noriGame) {
        for (Node node : grid.getChildren()) {
            NoriCell cell = noriGame.getCell(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            switch (cell.getState()) {
                case UNMARKED:
                    node.setStyle("-fx-background-color: lightgray;");
                    break;
                case MARKED_WHITE:
                    node.setStyle("-fx-background-color: white;");
                    break;
                case MARKED_BLACK:
                    node.setStyle("-fx-background-color: gray;");
                    break;
            }
        }
    }

    // TODO: This function should receive the board from the model with all the cells
    public void createBoard(NoriGame noriGame) {
        rows = noriGame.getMaxRow() + 1;  // Needed because maxRow == 0 needs 1 row and maxRow == 1 needs 2 rows
        columns = noriGame.getMaxColumn() + 1;  // Same logic as line above

        grid.setSnapToPixel(false);  // Needed for clearer and sharper edges in GUI

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
        for (NoriCell noriCell : noriGame.getNoriCellList()) {
            double topBorder, rightBorder, botBorder, leftBorder;
            topBorder = rightBorder = botBorder = leftBorder = BORDER_THICKNESS_NORMAL;

            int curCol = noriCell.getCol();
            int curRow = noriCell.getRow();
            int curRegionId = noriCell.getRegionId();

            // Check top border
            if (curRow <= 0 || noriGame.getCell(curCol, curRow - 1).getRegionId() != curRegionId) {
                topBorder = BORDER_THICKNESS_THICK;
            }
            // Check right border
            if (curCol >= columns - 1 || noriGame.getCell(curCol + 1, curRow).getRegionId() != curRegionId) {
                rightBorder = BORDER_THICKNESS_THICK;
            }
            // Check bottom border
            if (curRow >= rows - 1 || noriGame.getCell(curCol, curRow + 1).getRegionId() != curRegionId) {
                botBorder = BORDER_THICKNESS_THICK;
            }
            // Check left border
            if (curCol <= 0 || noriGame.getCell(curCol - 1, curRow).getRegionId() != curRegionId) {
                leftBorder = BORDER_THICKNESS_THICK;
            }

            Pane guiCell = new Pane();
            drawBorder(guiCell, topBorder, rightBorder, botBorder, leftBorder);
            grid.add(guiCell, curCol, curRow);
        }

        colorCells(noriGame);
    }
}
