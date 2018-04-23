package application.controller;

import application.util.ViewNavigator;

public class TeamController extends FxmlController {

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.TEAM_DETAIL);
    }

}
