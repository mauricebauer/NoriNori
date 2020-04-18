package norinori.model;

public interface IUiHelper {
    void updateCellColor(INoriGame noriGame);

    void startedSolving();

    void finishedSolving(INoriGame noriGame, boolean stepping);

    void finishedSolving(INoriGame noriGame, boolean stepping, boolean steppingResult);
}