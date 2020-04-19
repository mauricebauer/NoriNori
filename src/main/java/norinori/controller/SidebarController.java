package norinori.controller;

public class SidebarController {
    private final MainWindowController controller;

    /**
     * Constructor of the SidebarController class
     * @param mainWindowController Reference to the main controller (for other references)
     */
    public SidebarController(MainWindowController mainWindowController) {
        this.controller = mainWindowController;
    }

    /**
     * Sets the disabled property of the GUI buttons
     * @param disableStepButton True if the button should be disabled
     * @param disableSolveButton True if the button should be disabled
     * @param disableClearButton True if the button should be disabled
     */
    public void setButtons(boolean disableStepButton, boolean disableSolveButton, boolean disableClearButton) {
        controller.stepButton.setDisable(disableStepButton);
        controller.solveButton.setDisable(disableSolveButton);
        controller.clearButton.setDisable(disableClearButton);
    }

    /**
     * Shows the stats grid in the GUI and updates its values
     * @param boardWidth Number of columns of the current game
     * @param boardHeight Number of rows of the current game
     * @param regions Number of regions of the current game
     */
    public void setStats(int boardWidth, int boardHeight, int regions) {
        controller.statisticsGrid.setVisible(true);
        controller.sizeLabel.setText(boardWidth + "x" + boardHeight);
        controller.regionsLabel.setText(String.valueOf(regions));
    }
}
