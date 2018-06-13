package controller;

import application.MainApplication;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import util.ViewNavigator;

public abstract class FxmlController {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected abstract void initialize();

    public void initForShow() {
        MainApplication.instance.getMainController().initForShow();
    }

    protected void showDetail(ViewNavigator.NodeName nodeName, String headerText) {
        MainApplication.instance.getMainController().switchToDetailNode(nodeName, headerText);
    }

}
