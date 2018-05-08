package application.controller;

import application.util.ViewNavigator;

public class ProfileController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.PROFILE_DETAIL, "Profile detail");
    }

}
