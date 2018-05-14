package controller;

import util.ViewNavigator;

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
