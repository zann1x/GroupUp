package application.controller;

import application.MainApplication;
import application.util.ViewNavigator;

public class NewsController extends FxmlController {

    @Override
    public void showDetail() {
        MainApplication.getMainController().switchToDetailNode(ViewNavigator.NodeName.NEWS_DETAIL);
    }

}
