package application.controller;

import application.util.ViewNavigator;

public class NewsController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.NEWS_DETAIL, "News detail");
    }

}
