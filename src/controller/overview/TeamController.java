package controller.overview;

import controller.FxmlController;
import javafx.fxml.FXML;
import util.ViewNavigator;

public class TeamController extends FxmlController {

    @FXML
    protected void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @FXML
    private void createNewTeam() {
        super.showDetail(ViewNavigator.NodeName.TEAM_CREATION, "Team creation");
    }

    @FXML
    private void showTeams() {
        super.showDetail(ViewNavigator.NodeName.OWN_TEAMS, "Teams");
    }

}
