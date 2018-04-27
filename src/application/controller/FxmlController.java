package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class FxmlController {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected abstract void showDetail();

    protected void showDetail(ViewNavigator.NodeName nodeName, String headerText) {
        MainApplication.instance.getMainController().switchToDetailNode(nodeName, headerText);
    }

}
