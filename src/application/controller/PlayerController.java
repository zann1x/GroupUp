package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;

public class PlayerController extends FxmlController {

    @Override
    public void showDetail() {
        MainApplication.getMainController().switchToDetailNode(ViewNavigator.NodeName.PLAYER_DETAIL);
    }

}
