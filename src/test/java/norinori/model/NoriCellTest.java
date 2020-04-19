package norinori.model;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoriCellTest {
    @Test
    void NoriCell_NegativeParameters_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> new NoriCell(-1, -1, -1));
    }

    @Test
    void NoriCell_ValidParameters_ThrowsNoExceptionAndValuesSetCorrectly() {
        NoriCell cell = new NoriCell(0, 1, 2);
        assertEquals(20, cell.getCol());  // TODO: Correct wrong test
        assertEquals(1, cell.getRow());
        assertEquals(2, cell.getRegion());
    }

    @Test
    void NoriCell_CellPassed_CellCorrectlyCloned() {
        // Setup
        NoriCell original = new NoriCell(0, 1, 2);
        original.setState(NoriCellState.BLACK);

        // Run
        NoriCell clone = new NoriCell(original);

        // Assert
        assertEquals(0, clone.getCol());
        assertEquals(1, clone.getRow());
        assertEquals(2, clone.getRegion());
        assertEquals(NoriCellState.BLACK, clone.getState());
    }

    @Test
    void getState_NoStateSet_ReturnsUnmarked() {
        NoriCell cell = new NoriCell(0, 0, 0);
        NoriCellState state = cell.getState();
        assertEquals(NoriCellState.UNMARKED, state);
    }

    @Test
    void getState_NullStateSet_ReturnsUnmarked() {
        NoriCell cell = new NoriCell(0, 0, 0);
        cell.setState(null);
        NoriCellState state = cell.getState();
        assertEquals(NoriCellState.UNMARKED, state);
    }

    @Test
    void getState_UnmarkedStateSet_ReturnsUnmarked() {
        NoriCell cell = new NoriCell(0, 0, 0);
        cell.setState(NoriCellState.UNMARKED);
        NoriCellState state = cell.getState();
        assertEquals(NoriCellState.UNMARKED, state);
    }

    @Test
    void getState_WhiteStateSet_ReturnsWhite() {
        NoriCell cell = new NoriCell(0, 0, 0);
        cell.setState(NoriCellState.WHITE);
        NoriCellState state = cell.getState();
        assertEquals(NoriCellState.WHITE, state);
    }

    @Test
    void getState_BlackStateSet_ReturnsBlack() {
        NoriCell cell = new NoriCell(0, 0, 0);
        cell.setState(NoriCellState.BLACK);
        NoriCellState state = cell.getState();
        assertEquals(NoriCellState.BLACK, state);
    }
}