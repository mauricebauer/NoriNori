package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Button solveButton, stepButton, clearButton;

    @FXML
    private Label stateLabel;

    @FXML
    private void solveButtonClicked() {
        System.out.println("Solve");
    }

    @FXML
    private void stepButtonClicked() {
        System.out.println("Step");
    }

    @FXML
    private void clearButtonClicked() {
        System.out.println("Clear");
    }
}
