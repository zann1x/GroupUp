package controller.overview;

import controller.FxmlController;
import javafx.fxml.FXML;
import util.ViewNavigator;

public class NewsController extends FxmlController {

    @Override
    protected void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @FXML
    private void showDetail() {
        super.showDetail(ViewNavigator.NodeName.NEWS_DETAIL, "News detail");
    }

}
