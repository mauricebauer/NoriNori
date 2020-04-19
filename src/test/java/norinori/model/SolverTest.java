package norinori.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolverTest {
    @Test
    void reset_StackFilled_StackNowEmpty() {
        Solver solver = new Solver();
        solver.stack.push(new NoriCell(0, 0, 0));
        assertEquals(1, solver.stack.size());
        solver.reset();
        assertEquals(0, solver.stack.size());
    }

    @Test
    void solve_3x3Solvable_CorrectlySolved() throws IOException {
        // Setup
        Solver solver = new Solver();
        solver.uiUpdateFrequency = 1;
        String json = new String(getClass().getClassLoader().getResourceAsStream("3x3.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run
        solver.solve(game, null, false);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "XX0,00X,00X"));
    }

    @Test
    void solve_3x3AlreadySolved_StaysSolved() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("3x3.json").readAllBytes());
        NoriGame game = new NoriGame(json);
        game.getCell(0, 0).setState(NoriCellState.BLACK);
        game.getCell(1, 0).setState(NoriCellState.BLACK);
        game.getCell(2, 0).setState(NoriCellState.WHITE);
        game.getCell(0, 1).setState(NoriCellState.WHITE);
        game.getCell(1, 1).setState(NoriCellState.WHITE);
        game.getCell(2, 1).setState(NoriCellState.BLACK);
        game.getCell(0, 2).setState(NoriCellState.WHITE);
        game.getCell(1, 2).setState(NoriCellState.WHITE);
        game.getCell(2, 2).setState(NoriCellState.BLACK);

        // Run
        solver.solve(game, null, false);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "XX0,00X,00X"));
    }

    @Test
    void solve_3x3SolvableStepped_CorrectlyStepped() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("3x3.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run & Assert
        solver.solve(game, null, true);
        assertTrue(areStatesOfCellsEqual(game, "X--,---,---"));
        solver.solve(game, null, true);
        assertTrue(areStatesOfCellsEqual(game, "XX-,---,---"));
        solver.solve(game, null, true);
        assertTrue(areStatesOfCellsEqual(game, "XX0,---,---"));
    }

    @Test
    void solve_3x3SolvedThroughStepping_CorrectlyStepped() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("3x3.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run
        while (!game.isSolved())
            solver.solve(game, null, true);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "XX0,00X,00X"));
    }

    @Test
    void solve_3x3NotSolvable_NotSolved() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("3x3_NotSolvable.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run
        solver.solve(game, null, false);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "---,---,---"));
    }

    @Test
    void solve_4x3Solvable_CorrectlySolved() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("4x3.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run
        solver.solve(game, null, false);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "XX00,000X,XX0X"));
    }

    @Test
    void solve_6x6Solvable_CorrectlySolved() throws IOException {
        // Setup
        Solver solver = new Solver();
        String json = new String(getClass().getClassLoader().getResourceAsStream("6x6.json").readAllBytes());
        NoriGame game = new NoriGame(json);

        // Run
        solver.solve(game, null, false);

        // Assert
        assertTrue(areStatesOfCellsEqual(game, "XX0X0X,000X0X,XX0000,00XX0X,00000X,XX0XX0"));
    }

    // stateString example: 00X,XX0,X--
    private boolean areStatesOfCellsEqual(NoriGame game, String stateString) {
        stateString = stateString.replace(",", "");
        for (int i = 0; i < stateString.length(); i++) {
            NoriCellState expected = stateString.charAt(i) == 'X' ?
                    NoriCellState.BLACK :
                    (stateString.charAt(i) == '0' ?
                            NoriCellState.WHITE :
                            NoriCellState.UNMARKED);
            if (game.getNoriCells().get(i).getState() != expected) return false;
        }
        return true;
    }
}