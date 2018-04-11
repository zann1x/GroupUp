package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;

public class TeamController extends FxmlController {

    @Override
    public void showDetail() {
        MainApplication.getMainController().switchToDetailNode(ViewNavigator.NodeName.TEAM_DETAIL);
    }

}
