package norinori.model;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class NoriGameTest {
    @Test
    void NoriGame_EmptyStringPassed_ThrowsException() {
        assertThrows(Exception.class, () -> new NoriGame(""));
    }

    @Test
    void NoriGame_InvalidStringPassed_ThrowsException() {
        assertThrows(Exception.class, () -> new NoriGame("{}"));
    }

    @Test
    void NoriGame_InvalidJsonPassed_ThrowsException() {
        assertThrows(Exception.class, () -> new NoriGame("["));
    }

    @Test
    void NoriGame_NotSquarePassed_ThrowsException() {
        assertThrows(Exception.class, () -> new NoriGame("[[0,0],[0]]"));
    }

    @Test
    void NoriGame_3x3OneRegionPassed_CorrectGameCreated() {
        // Setup & Run
        NoriGame game = new NoriGame("[[0,0,1],[1,1,1],[1,1,1]]");
        // Assert
        assertEquals(9, game.getNoriCells().size());
        assertEquals(1, game.getMaxRegion());
        for (NoriCell cell : game.getNoriCells()) {
            assertEquals(NoriCellState.UNMARKED, cell.getState());
            assertTrue(cell.getCol() <= 2 && cell.getRow() <= 2 && cell.getRegion() <= 1);
        }
    }

    @Test
    void NoriGame_6x6Region20Passed_CorrectGameCreated() {
        // Setup & Run
        NoriGame game = new NoriGame("[\n" +
                "  [0,0,0,1,1,1],\n" +
                "  [0,2,2,3,3,3],\n" +
                "  [2,2,4,4,5,5],\n" +
                "  [6,4,4,4,5,5],\n" +
                "  [6,6,6,6,5,5],\n" +
                "  [20,20,6,6,6,6]\n" +
                "]");
        // Assert
        assertEquals(36, game.getNoriCells().size());
        assertEquals(20, game.getMaxRegion());
        for (NoriCell cell : game.getNoriCells()) {
            assertEquals(NoriCellState.UNMARKED, cell.getState());
            assertTrue(cell.getCol() <= 6 && cell.getRow() <= 6 && cell.getRegion() <= 20);
        }
    }

    @Test
    void isSolved_SolvedBoard_ReturnsTrue() {
        // Setup
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);

        // Run & Assert
        assertTrue(game.isSolved());
    }

    @Test
    void isSolved_BoardWithNullsPassed_ReturnsFalse() {
        // Setup
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.WHITE);
        game.getNoriCells().get(1).setState(null);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        game.getNoriCells().get(3).setState(NoriCellState.BLACK);

        // Run & Assert
        assertFalse(game.isSolved());
    }

    @Test
    void resetCells_SolvedBoardPassed_BoardCompletelyUnmarked() {
        // Setup
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);

        // Run
        game.resetCells();

        // Assert
        for (NoriCell cell : game.getNoriCells()) {
            assertEquals(NoriCellState.UNMARKED, cell.getState());
        }
    }

    @Test
    void checkStateAtCell_CheckUnmarkedState_ThrowsException() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        assertThrows(InvalidParameterException.class, () -> game.checkStateAtCell(game.getNoriCells().get(0), NoriCellState.UNMARKED));
    }

    @Test
    void checkStateAtCell_2of4CellsWhite_WhitePlacementNotPossible() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.WHITE);
        game.getNoriCells().get(1).setState(NoriCellState.WHITE);
        assertFalse(game.checkStateAtCell(game.getNoriCells().get(2), NoriCellState.WHITE));
    }

    @Test
    void checkStateAtCell_2of4CellsWhite_BlackPlacementPossible() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.WHITE);
        game.getNoriCells().get(1).setState(NoriCellState.WHITE);
        assertTrue(game.checkStateAtCell(game.getNoriCells().get(2), NoriCellState.BLACK));
    }

    @Test
    void checkStateAtCell_3x3NoDominoNearEachOtherAllowed_BlackPlacementNotPossible() {
        NoriGame game = new NoriGame("[[0,0,1],[1,1,1],[1,1,1]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);
        assertFalse(game.checkStateAtCell(game.getNoriCells().get(4), NoriCellState.BLACK));
    }

    @Test
    void checkStateAtCell_3x3NoLonelyCellsShouldBeConnected_BlackPlacementNotPossible() {
        NoriGame game = new NoriGame("[[0,0,0],[0,1,1],[1,1,1]]");
        game.getNoriCells().get(0).setState(NoriCellState.WHITE);
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        game.getNoriCells().get(3).setState(NoriCellState.BLACK);
        assertFalse(game.checkStateAtCell(game.getNoriCells().get(4), NoriCellState.BLACK));
    }

    @Test
    void checkStateAtCell_3x3LonelyBlackCellShouldNotBeEncapsulated_WhitePlacementNotPossible() {
        NoriGame game = new NoriGame("[[0,0,0],[0,0,1],[1,1,1]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(1).setState(NoriCellState.WHITE);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        assertFalse(game.checkStateAtCell(game.getNoriCells().get(3), NoriCellState.WHITE));
    }

    @Test
    void isMoreThanOneLonelyBlackCellAround_4BlackCellsAround_ReturnTrue() {
        NoriGame game = new NoriGame("[[0,0,0],[0,0,0],[0,0,0]]");
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(3).setState(NoriCellState.BLACK);
        game.getNoriCells().get(5).setState(NoriCellState.BLACK);
        game.getNoriCells().get(7).setState(NoriCellState.BLACK);
        assertTrue(game.isMoreThanOneLonelyBlackCellAround(game.getNoriCells().get(4)));
    }

    @Test
    void isMoreThanOneLonelyBlackCellAround_1BlackCellAround_ReturnFalse() {
        NoriGame game = new NoriGame("[[0,0,0],[0,0,0],[0,0,0]]");
        game.getNoriCells().get(4).setState(NoriCellState.BLACK);
        game.getNoriCells().get(7).setState(NoriCellState.BLACK);
        assertFalse(game.isMoreThanOneLonelyBlackCellAround(game.getNoriCells().get(8)));
    }

    @Test
    void willThisPlacementEncapsulateALonelyBlackCell_YesAtTop_ReturnsTrue() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(1).setState(NoriCellState.WHITE);
        assertTrue(game.willThisPlacementEncapsulateALonelyBlackCell(game.getNoriCells().get(2)));
    }

    @Test
    void willThisPlacementEncapsulateALonelyBlackCell_YesRight_ReturnsTrue() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);
        assertTrue(game.willThisPlacementEncapsulateALonelyBlackCell(game.getNoriCells().get(0)));
    }

    @Test
    void willThisPlacementEncapsulateALonelyBlackCell_YesLeft_ReturnsTrue() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.WHITE);
        assertTrue(game.willThisPlacementEncapsulateALonelyBlackCell(game.getNoriCells().get(1)));
    }

    @Test
    void willThisPlacementEncapsulateALonelyBlackCell_YesBottom_ReturnsTrue() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(2).setState(NoriCellState.BLACK);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);
        assertTrue(game.willThisPlacementEncapsulateALonelyBlackCell(game.getNoriCells().get(0)));
    }

    @Test
    void willThisPlacementEncapsulateALonelyBlackCell_No_ReturnsFalse() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        assertFalse(game.willThisPlacementEncapsulateALonelyBlackCell(game.getNoriCells().get(2)));
    }

    @Test
    void willThisPlacementEncapsulateTheGivenCell_TopNo_ReturnsFalse() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(0).setState(NoriCellState.BLACK);
        game.getNoriCells().get(2).setState(NoriCellState.BLACK);
        assertFalse(game.willThisPlacementEncapsulateTheGivenCell(0, 1, NoriDominoDirection.RIGHT));
    }

    @Test
    void willThisPlacementEncapsulateTheGivenCell_BottomNo_ReturnsFalse() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(2).setState(NoriCellState.BLACK);
        game.getNoriCells().get(3).setState(NoriCellState.BLACK);
        assertFalse(game.willThisPlacementEncapsulateTheGivenCell(1, 0, NoriDominoDirection.TOP));
    }

    @Test
    void willThisPlacementEncapsulateTheGivenCell_RightYes_ReturnsTrue() {
        NoriGame game = new NoriGame("[[0,0],[0,0]]");
        game.getNoriCells().get(1).setState(NoriCellState.BLACK);
        game.getNoriCells().get(3).setState(NoriCellState.WHITE);
        assertTrue(game.willThisPlacementEncapsulateTheGivenCell(1, 0, NoriDominoDirection.LEFT));
    }
}