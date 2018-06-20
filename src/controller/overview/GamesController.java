package controller.overview;

import controller.FxmlController;
import javafx.fxml.FXML;
import util.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    protected void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @FXML
    public void playNow() {
        super.showDetail(ViewNavigator.NodeName.PLAY_NOW, "Play now");
    }

    @FXML
    private void unLinkGames() {
        super.showDetail(ViewNavigator.NodeName.GAME_LINKING, "Un/Link games");
    }

}
