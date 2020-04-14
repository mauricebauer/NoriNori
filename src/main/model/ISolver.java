package main.model;

import java.util.List;

public interface ISolver {
    // solve() returns true if the NoriNori could be solved
    boolean solve(List<NoriCell> cells, boolean doOnlyOneStep);

    void reset();
}
