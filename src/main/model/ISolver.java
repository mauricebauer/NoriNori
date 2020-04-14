package main.model;

import java.util.List;

public interface ISolver {
    void solve(List<NoriCell> cells, boolean doOnlyOneStep);
}
