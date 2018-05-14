package application.controller;

import application.util.navigation.ViewNavigator;

public class NewsController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.NEWS_DETAIL, "News detail");
    }

}
