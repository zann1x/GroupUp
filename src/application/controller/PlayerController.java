package application.controller;

import application.util.ViewNavigator;

public class PlayerController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.PLAYER_DETAIL, "Player detail");
    }

}
