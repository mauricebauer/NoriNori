package main.model;

import java.util.List;

public class BacktrackingSolver implements ISolver {
    @Override
    public boolean solve(List<NoriCell> cells, boolean doOnlyOneStep) {
        if (!doOnlyOneStep)
            reset();


        return false;
    }

    @Override
    public void reset() {
        // TODO: Reset private fields with current position and history
    }
}
