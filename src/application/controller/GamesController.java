package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    public void showDetail() {
        MainApplication.getMainController().switchToDetailNode(ViewNavigator.NodeName.GAMES_DETAIL);
    }

}
