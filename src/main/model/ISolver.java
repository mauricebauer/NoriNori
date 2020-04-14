package main.model;

public interface ISolver {
    // solve() returns true if the NoriNori could be solved
    boolean solve(NoriGame noriGame, boolean doOnlyOneStep);

    void reset();
}
