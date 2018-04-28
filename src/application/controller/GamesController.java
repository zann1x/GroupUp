package application.controller;

import application.util.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.GAMES_DETAIL, "Game detail");
    }
}
