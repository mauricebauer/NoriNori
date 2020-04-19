package norinori.model;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class NoriRegionTest {
    @Test
    void addCell_EmptyCellListAtBeginning_CellCountIncrements() {
        NoriRegion region = new NoriRegion();
        assertEquals(0, region.cellList.size());
        NoriCell cell1 = new NoriCell(0, 0, 0);
        region.addCell(cell1);
        assertEquals(1, region.cellList.size());
        assertEquals(cell1, region.cellList.get(0));
    }

    @Test
    void addCell_TryToAddDifferentRegions_ThrowsException() {
        NoriRegion region = new NoriRegion();
        region.addCell(new NoriCell(0, 0, 0));
        assertThrows(InvalidParameterException.class, () -> region.addCell(new NoriCell(1, 0, 1)));
    }

    @Test
    void getNumberOfCells_TwoBlackOneWhiteZeroUnmarked_CorrectNumberReturned() {
        // Setup
        NoriRegion region = new NoriRegion();
        region.addCell(new NoriCell(0, 0, 0) {{setState(NoriCellState.BLACK);}});
        region.addCell(new NoriCell(1, 0, 0) {{setState(NoriCellState.BLACK);}});
        region.addCell(new NoriCell(2, 0, 0) {{setState(NoriCellState.WHITE);}});

        // Run & Assert
        assertEquals(0, region.getNumberOfCells(NoriCellState.UNMARKED));
        assertEquals(1, region.getNumberOfCells(NoriCellState.WHITE));
        assertEquals(2, region.getNumberOfCells(NoriCellState.BLACK));
    }

    @Test
    void checkIfPlacementIsValid_DifferentRegions() {
        NoriRegion region0 = createRegionFromString("UUU");
        assertTrue(region0.checkIfPlacementIsValid(NoriCellState.WHITE), "UUU - White must be possible");
        assertTrue(region0.checkIfPlacementIsValid(NoriCellState.BLACK), "UUU - Black must be possible");
        assertTrue(region0.checkIfPlacementIsValid(NoriCellState.UNMARKED), "UUU - Unmarked must be possible");

        NoriRegion region1 = createRegionFromString("WUU");
        assertFalse(region1.checkIfPlacementIsValid(NoriCellState.WHITE), "WUU - White must not be possible");
        assertTrue(region1.checkIfPlacementIsValid(NoriCellState.BLACK), "WUU - Black must be possible");
        assertFalse(region1.checkIfPlacementIsValid(NoriCellState.UNMARKED), "WUU - Unmarked must not be possible");

        NoriRegion region2 = createRegionFromString("BUU");
        assertTrue(region2.checkIfPlacementIsValid(NoriCellState.WHITE), "BUU - White must be possible");
        assertTrue(region2.checkIfPlacementIsValid(NoriCellState.BLACK), "BUU - Black must be possible");
        assertTrue(region2.checkIfPlacementIsValid(NoriCellState.UNMARKED), "BUU - Unmarked must be possible");

        NoriRegion region3 = createRegionFromString("WBU");
        assertFalse(region3.checkIfPlacementIsValid(NoriCellState.WHITE), "WBU - White must not be possible");
        assertTrue(region3.checkIfPlacementIsValid(NoriCellState.BLACK), "WBU - Black must be possible");
        assertFalse(region3.checkIfPlacementIsValid(NoriCellState.UNMARKED), "WBU - Unmarked must not be possible");

        NoriRegion region4 = createRegionFromString("BBU");
        assertTrue(region4.checkIfPlacementIsValid(NoriCellState.WHITE), "BBU - White must be possible");
        assertFalse(region4.checkIfPlacementIsValid(NoriCellState.BLACK), "BBU - Black must not be possible");
        assertTrue(region4.checkIfPlacementIsValid(NoriCellState.UNMARKED), "BBU - Unmarked must be possible");

        NoriRegion region5 = createRegionFromString("BBW");
        assertFalse(region5.checkIfPlacementIsValid(NoriCellState.WHITE), "BBW - White must not be possible");
        assertFalse(region5.checkIfPlacementIsValid(NoriCellState.BLACK), "BBW - Black must not be possible");
        assertFalse(region5.checkIfPlacementIsValid(NoriCellState.UNMARKED), "BBW - Unmarked must not be possible");
    }

    private NoriRegion createRegionFromString(String input) {
        NoriRegion region = new NoriRegion();
        int col = 0;
        for (char c : input.toCharArray()) {
            region.addCell(new NoriCell(col++, 0, 0) {{
                setState(c == 'U' ? NoriCellState.UNMARKED : (c == 'B' ? NoriCellState.BLACK : NoriCellState.WHITE));
            }});
        }
        return region;
    }
}