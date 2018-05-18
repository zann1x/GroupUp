package controller.overview;

import controller.FxmlController;
import javafx.fxml.FXML;
import util.ViewNavigator;

public class GamesController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @FXML
    public void addGame() {
        super.showDetail(ViewNavigator.NodeName.ADD_GAME, "Game linking");
    }

    @FXML
    private void showGames() {
        super.showDetail(ViewNavigator.NodeName.LINKED_GAMES, "Linked games");
    }

}
