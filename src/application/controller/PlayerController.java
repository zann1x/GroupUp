package application.controller;

import application.util.ViewNavigator;

public class PlayerController extends FxmlController {

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.PLAYER_DETAIL);
    }

}
