package controller.detail.team;

import application.Session;
import controller.FxmlController;
import javafx.event.ActionEvent;
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
    private TreeItem<String> rootItem = new TreeItem<>("Teams");

    private List<Team> activeTeams = new ArrayList<>();
    private List<Team> inactiveTeams = new ArrayList<>();

    private Map<Integer, List<Player>> playerMap = new TreeMap<>();
    private static final String ACTIVE_TEAM_STRING = "Active teams";
    private static final String INACTIVE_TEAM_STRING = "Inactive teams";

    @FXML
    public void initialize() {
    }

    private void initOwnTeamsView() {
        try {
            collectTeamsAndPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTreeViewBody();

        tv_teams.setCellFactory(t -> new CustomTreeCell());
        tv_teams.setRoot(rootItem);
        tv_teams.setShowRoot(false);
    }

    private void collectTeamsAndPlayers() throws SQLException {
        List<Player> playersInTeam;
        List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();

        for (int id : teamIds) {
            // initialize the list at each iteration, so every team gets its own list of players
            playersInTeam = new ArrayList<>();
            try {
                Team team = new Team(id);
                if (team.isActive())
                    activeTeams.add(team);
                else
                    inactiveTeams.add(team);
                playersInTeam = team.getPlayers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            playersInTeam.sort(Comparator.comparing(Player::toString));
            playerMap.put(id, playersInTeam);
        }

        activeTeams.sort(Comparator.comparing(Team::toString));
        inactiveTeams.sort(Comparator.comparing(Team::toString));
    }

    private void createTreeViewBody() {
        // populate tree view with all collected teams
        TreeItem<String> activeTeamsItem = new TreeItem<>(ACTIVE_TEAM_STRING);
        TreeItem<String> inactiveTeamsItem = new TreeItem<>(INACTIVE_TEAM_STRING);

        List<TreeItem<String>> treeItems = createTreeItemList(activeTeams);
        for (TreeItem<String> treeItem : treeItems)
            activeTeamsItem.getChildren().add(treeItem);

        treeItems = createTreeItemList(inactiveTeams);
        for (TreeItem<String> treeItem : treeItems)
            inactiveTeamsItem.getChildren().add(treeItem);

        activeTeamsItem.setExpanded(true);
        inactiveTeamsItem.setExpanded(true);
        rootItem.getChildren().add(activeTeamsItem);
        rootItem.getChildren().add(inactiveTeamsItem);
    }

    private List<TreeItem<String>> createTreeItemList(List<Team> teams) {
        List<TreeItem<String>> treeItems = new ArrayList<>();

        for (Team team : teams) {
            List<Player> playersInTeam = playerMap.get(team.getId());
            TreeItem<String> teamItem = new TreeItem<>(team.getName());
            for (Player player : playersInTeam) {
                TreeItem<String> playerItem = new TreeItem<>(player.getPseudonym());
                teamItem.getChildren().add(playerItem);
            }
            treeItems.add(teamItem);
        }
        return treeItems;
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

        private ContextMenu activeTeamsContextMenu;
        private ContextMenu inactiveTeamsContextMenu;
        private ContextMenu playerContextMenu;

        public CustomTreeCell() {
            MenuItem addPlayer = new MenuItem("Add player");
            addPlayer.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getValue());
                    new AddPlayerPopup(team).showAndWait();
                    initForShow();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            javafx.event.EventHandler<ActionEvent> deleteTeamEventHandler = actionEvent -> {
                try {
                    Team team = new Team(getTreeItem().getValue());
                    team.delete();
                    initForShow();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            };

            MenuItem deleteTeam = new MenuItem("Delete team");
            deleteTeam.setOnAction(deleteTeamEventHandler);

            MenuItem deleteTeam2 = new MenuItem("Delete team");
            deleteTeam2.setOnAction(deleteTeamEventHandler);

            MenuItem setInactive = new MenuItem("Set inactive");
            setInactive.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getValue());
                    team.setActive(false);
                    initForShow();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            MenuItem setActive = new MenuItem("Set active");
            setActive.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getValue());
                    team.setActive(true);
                    initForShow();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            MenuItem removePlayer = new MenuItem("Remove player");
            removePlayer.setOnAction(e -> {
                try {
                    Team team = new Team(getTreeItem().getParent().getValue());
                    Player player = new Player(getTreeItem().getValue());
                    team.removePlayer(player);
                    initForShow();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            activeTeamsContextMenu = new ContextMenu(addPlayer, deleteTeam, setInactive);
            inactiveTeamsContextMenu = new ContextMenu(deleteTeam2, setActive);
            playerContextMenu = new ContextMenu(removePlayer);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                if (!getTreeItem().isLeaf()) {
                    if (getTreeItem().getParent().getValue().equals(ACTIVE_TEAM_STRING))
                        setContextMenu(activeTeamsContextMenu);
                    else if (getTreeItem().getParent().getValue().equals(INACTIVE_TEAM_STRING))
                        setContextMenu(inactiveTeamsContextMenu);
                } else {
                    setContextMenu(playerContextMenu);
                }
            }
        }
    }

}
