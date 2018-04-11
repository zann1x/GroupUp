package application.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class FxmlController {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public abstract void showDetail();

}
