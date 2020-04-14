package main.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NoriGame {
    private final List<NoriCell> noriCellList = new ArrayList<>();

    public NoriGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                noriCellList.add(new NoriCell(i, j, 0));
            }
        }
    }

    public NoriGame(String jsonString) {
        Type jsonType = new TypeToken<ArrayList<ArrayList<Integer>>>() {
        }.getType();
        ArrayList<ArrayList<Integer>> input = new Gson().fromJson(jsonString, jsonType);
        noriCellList.clear();
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).size(); col++) {
                noriCellList.add(new NoriCell(col, row, input.get(row).get(col)));
            }
        }
    }

    public void resetCells() {
        for (NoriCell cell : noriCellList) {
            cell.setState(NoriCellState.UNMARKED);
        }
    }

    public List<NoriCell> getNoriCellList() {
        return noriCellList;
    }

    public NoriCell getCell(int col, int row) {
        return noriCellList.stream().filter(c -> c.getCol() == col && c.getRow() == row).findFirst().get();
    }

    public int getMaxRow() {
        return noriCellList.stream().map(NoriCell::getRow).max(Integer::compareTo).get();
    }

    public int getMaxColumn() {
        return noriCellList.stream().map(NoriCell::getCol).max(Integer::compareTo).get();
    }
}
