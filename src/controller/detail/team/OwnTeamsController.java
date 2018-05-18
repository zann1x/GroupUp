package controller.detail.team;

import application.Session;
import controller.FxmlController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    public VBox vb_statistics;
    @FXML
    public Label lbl_statistics;

    private TreeItem<String> rootItem;

    private List<Team> activeTeams;
    private List<Team> inactiveTeams;

    private Map<Integer, List<Player>> playerMap;

    private static final String ACTIVE_TEAM_STRING = "Active teams";
    private static final String INACTIVE_TEAM_STRING = "Inactive teams";
    private static final String ROOT_STRING = "Teams";

    @FXML
    public void initialize() {
        activeTeams = new ArrayList<>();
        inactiveTeams = new ArrayList<>();
        rootItem = new TreeItem<>(ROOT_STRING);
        playerMap = new TreeMap<>();
    }

    private void initOwnTeamsView() {
        try {
            collectTeamsAndPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTreeViewBody();

        tv_teams.setCellFactory(t -> new CustomTreeCell());
        tv_teams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String val = newValue.getValue();
                if (!(val.equals(ROOT_STRING) || val.equals(ACTIVE_TEAM_STRING) || val.equals(INACTIVE_TEAM_STRING))) {
                    lbl_statistics.setText("Statistics of " + newValue.getValue());
                    vb_statistics.setVisible(true);
                }
            }
        });
        tv_teams.setRoot(rootItem);
        tv_teams.setShowRoot(false);
    }

    private void collectTeamsAndPlayers() throws SQLException {
        List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();

        for (int id : teamIds) {
            // initialize the list at each iteration, so every team gets its own list of players
            List<Player> playersInTeam = new ArrayList<>();
            try {
                Team team = new Team(id);
                playersInTeam = team.getPlayers();

                if (team.isActive())
                    activeTeams.add(team);
                else
                    inactiveTeams.add(team);
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
            tv_teams.setRoot(null);
            vb_statistics.setVisible(false);
            lbl_statistics.setText("");

            rootItem.getChildren().clear();
            activeTeams.clear();
            inactiveTeams.clear();
            playerMap.clear();

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
                String parentValue = getTreeItem().getParent().getValue();
                if (getTreeItem().isLeaf()) {
                    if (!parentValue.equals(ROOT_STRING))
                        setContextMenu(playerContextMenu);
                } else {
                    if (parentValue.equals(ACTIVE_TEAM_STRING))
                        setContextMenu(activeTeamsContextMenu);
                    else if (parentValue.equals(INACTIVE_TEAM_STRING))
                        setContextMenu(inactiveTeamsContextMenu);
                }
            }
        }
    }

}
