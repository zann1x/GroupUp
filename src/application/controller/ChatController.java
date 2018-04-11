package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;
import javafx.fxml.FXML;

public class ChatController extends FxmlController {

    @Override
    @FXML
    public void showDetail() {
        MainApplication.getMainController().switchToDetailNode(ViewNavigator.NodeName.CHAT_DETAIL);
    }

}
