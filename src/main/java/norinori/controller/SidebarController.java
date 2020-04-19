package norinori.controller;

public class SidebarController {
    private final MainWindowController controller;

    public SidebarController(MainWindowController mainWindowController) {
        this.controller = mainWindowController;
    }

    public void setButtons(boolean disableStepButton, boolean disableSolveButton, boolean disableClearButton) {
        controller.stepButton.setDisable(disableStepButton);
        controller.solveButton.setDisable(disableSolveButton);
        controller.clearButton.setDisable(disableClearButton);
    }

    public void setStats(int boardWidth, int boardHeight, int regions) {
        controller.statisticsGrid.setVisible(true);
        controller.sizeLabel.setText(boardWidth + "x" + boardHeight);
        controller.regionsLabel.setText(String.valueOf(regions));
    }
}
