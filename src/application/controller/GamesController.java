package application.controller;

import application.util.navigation.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.GAMES_DETAIL, "Game detail");
    }
}
