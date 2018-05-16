package controller.detail.team;

import application.Session;
import controller.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import model.Player;
import model.Team;
import view.AddPlayerPopup;

import java.sql.SQLException;
import java.util.*;

public class OwnTeamsController extends FxmlController {

    @FXML
    private BorderPane ownTeamsRoot;

    @FXML
    private TreeView<String> tv_teams;

    @FXML
    public void initialize() {
    }

    private void initOwnTeamsView() {
        List<Team> ownTeams = new ArrayList<>();
        List<Player> playersInTeam;
        Map<Integer, List<Player>> playerMap = new TreeMap<>();

        try {
            List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();
            // collect own teams and all players in them
            for (int id : teamIds) {
                // initialize at each iteration, so every team gets its own list of players
                playersInTeam = new ArrayList<>();
                try {
                    Team team = new Team(id);
                    ownTeams.add(team);
                    playersInTeam = team.getPlayers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                playersInTeam.sort(Comparator.comparing(Player::toString));
                playerMap.put(id, playersInTeam);
            }
            ownTeams.sort(Comparator.comparing(Team::toString));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // populate tree view with all collected teams
        TreeItem<String> rootItem = new TreeItem<>("Teams");
        for (Team team : ownTeams) {
            playersInTeam = playerMap.get(team.getId());
            TreeItem<String> teamItem = new TreeItem<>(team.getName());
            for (Player player : playersInTeam) {
                TreeItem<String> playerItem = new TreeItem<>(player.getPseudonym());
                teamItem.getChildren().add(playerItem);
            }
            rootItem.getChildren().add(teamItem);
        }

        tv_teams.setCellFactory(t -> new CustomTreeCell());
        tv_teams.setRoot(rootItem);
        tv_teams.setShowRoot(false);
    }

    @Override
    public void initForShow() {
        if (ownTeamsRoot != null) {
            initOwnTeamsView();
        }
    }

    //////////////////////
    // CUSTOM TREE CELL //
    //////////////////////

    private class CustomTreeCell extends TextFieldTreeCell<String> {

        private ContextMenu teamContextMenu;
        private ContextMenu playerContextMenu;

        public CustomTreeCell() {
            MenuItem rootItem = new MenuItem("Add player");
            rootItem.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getValue());
                    new AddPlayerPopup(team).show();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            teamContextMenu = new ContextMenu(rootItem);

            MenuItem leafItem = new MenuItem("Remove player");
            leafItem.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getParent().getValue());
                    Player player = new Player(getTreeItem().getValue());
                    team.removePlayer(player);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            playerContextMenu = new ContextMenu(leafItem);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                if (!getTreeItem().isLeaf())
                    setContextMenu(teamContextMenu);
                else
                    setContextMenu(playerContextMenu);
            }
        }
    }
}
