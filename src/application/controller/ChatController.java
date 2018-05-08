package application.controller;

import application.util.ViewNavigator;

public class ChatController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.CHAT_DETAIL, "Chat detail");
    }

}
