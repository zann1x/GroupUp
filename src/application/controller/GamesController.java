package application.controller;

import application.util.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.GAMES_DETAIL);
    }

}
