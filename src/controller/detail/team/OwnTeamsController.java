package controller.detail.team;

import application.MainApplication;
import application.Session;
import controller.FxmlController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import model.Player;
import model.Team;
import view.alert.ErrorAlert;
import view.popup.playerpopup.AddPlayerToTeamPopup;
import view.popup.teampopup.EditTeamNamePopup;

import java.sql.SQLException;
import java.util.*;

public class OwnTeamsController extends FxmlController {

    private static final String ACTIVE_TEAM_STRING = "Active teams";
    private static final String INACTIVE_TEAM_STRING = "Inactive teams";
    private static final String ROOT_STRING = "Teams";

    @FXML
    public VBox vb_statistics;
    @FXML
    public Label lbl_statistics;
    @FXML
    private BorderPane ownTeamsRoot;
    @FXML
    private TreeView<Object> tv_teams;

    private TreeItem<Object> rootItem;
    private List<Team> activeTeams;
    private List<Team> inactiveTeams;

    private Map<Integer, List<Player>> playerMap;

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
        	ErrorAlert.showConnectionAlert();
            e.printStackTrace();
        }

        createTreeViewBody();

        tv_teams.setCellFactory(t -> new CustomTreeCell());
        tv_teams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String val = newValue.getValue().toString();
                if (!(val.equals(ROOT_STRING) || val.equals(ACTIVE_TEAM_STRING) || val.equals(INACTIVE_TEAM_STRING) || observable.getValue().isLeaf())) {
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
            Team team = new Team(id);
            if (team.isActive())
                activeTeams.add(team);
            else
                inactiveTeams.add(team);

            // initialize the list at each iteration, so every team gets its own list of players
            List<Player> playersInTeam = team.getPlayers();
            playersInTeam.sort(Comparator.comparing(Player::getPseudonym));

            playerMap.put(id, playersInTeam);
        }

        activeTeams.sort(Comparator.comparing(Team::getName));
        inactiveTeams.sort(Comparator.comparing(Team::getName));
    }

    private void createTreeViewBody() {
        // populate tree view with all collected teams
        TreeItem<Object> activeTeamsItem = new TreeItem<>(ACTIVE_TEAM_STRING);
        TreeItem<Object> inactiveTeamsItem = new TreeItem<>(INACTIVE_TEAM_STRING);

        List<TreeItem<Object>> treeItems = createTreeItemList(activeTeams);
        for (TreeItem<Object> treeItem : treeItems)
            activeTeamsItem.getChildren().add(treeItem);

        treeItems = createTreeItemList(inactiveTeams);
        for (TreeItem<Object> treeItem : treeItems)
            inactiveTeamsItem.getChildren().add(treeItem);

        activeTeamsItem.setExpanded(true);
        inactiveTeamsItem.setExpanded(true);
        if (!activeTeamsItem.getChildren().isEmpty())
            rootItem.getChildren().add(activeTeamsItem);
        if (!inactiveTeamsItem.getChildren().isEmpty())
            rootItem.getChildren().add(inactiveTeamsItem);
    }

    private List<TreeItem<Object>> createTreeItemList(List<Team> teams) {
        List<TreeItem<Object>> treeItems = new ArrayList<>();

        for (Team team : teams) {
            List<Player> playersInTeam = playerMap.get(team.getId());
            TreeItem<Object> teamItem = new TreeItem<>(team);

            for (Player player : playersInTeam) {
                TreeItem<Object> playerItem = new TreeItem<>(player);
                teamItem.getChildren().add(playerItem);
            }
            treeItems.add(teamItem);
        }
        return treeItems;
    }

    @Override
    public void initForShow() {
        super.initForShow();
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

    private class CustomTreeCell extends TextFieldTreeCell<Object> {

        private ContextMenu activeTeamsContextMenu;
        private ContextMenu inactiveTeamsContextMenu;
        private ContextMenu teamContextMenu;
        private ContextMenu playerContextMenu;
        private ContextMenu leaderContextMenu;

        public CustomTreeCell() {
            MenuItem renameTeam = new MenuItem("Rename");
            renameTeam.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getValue()).getId());
                    new EditTeamNamePopup(team).showAndWait();
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            MenuItem addPlayer = new MenuItem("Add player");
            addPlayer.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getValue()).getId());
                    new AddPlayerToTeamPopup(team).showAndWait();
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            javafx.event.EventHandler<ActionEvent> deleteTeamEventHandler = actionEvent -> {
                try {
                    if (showConfirmationAlert("Are you sure you want to delete the team?")) {
                        Team team = new Team(((Team) getTreeItem().getValue()).getId());
                        team.delete();
                    }
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            };

            MenuItem deleteActiveTeam = new MenuItem("Delete team");
            deleteActiveTeam.setOnAction(deleteTeamEventHandler);

            MenuItem deleteInactiveTeam = new MenuItem("Delete team");
            deleteInactiveTeam.setOnAction(deleteTeamEventHandler);

            MenuItem setInactive = new MenuItem("Set inactive");
            setInactive.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getValue()).getId());
                    team.setActive(false);
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            MenuItem setActive = new MenuItem("Set active");
            setActive.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getValue()).getId());
                    team.setActive(true);
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            MenuItem makeLeader = new MenuItem("Make leader");
            makeLeader.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getParent().getValue()).getId());
                    Player player = new Player(((Player) getTreeItem().getValue()).getId());
                    team.addLeader(player);
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            MenuItem removeLeader = new MenuItem("Remove leader");
            removeLeader.setOnAction(e -> {
                try {
                    Team team = new Team(((Team) getTreeItem().getParent().getValue()).getId());
                    Player player = new Player(((Player) getTreeItem().getValue()).getId());
                    team.removeLeader(player);
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            });

            javafx.event.EventHandler<ActionEvent> removePlayerEventHandler = actionEvent -> {
                try {
                    if (showConfirmationAlert("Are you sure you want to remove this player?")) {
                        Team team = new Team(((Team) getTreeItem().getParent().getValue()).getId());
                        Player player = new Player(((Player) getTreeItem().getValue()).getId());
                        team.removePlayer(player);
                    }
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            };

            MenuItem removePlayer = new MenuItem("Remove player");
            removePlayer.setOnAction(removePlayerEventHandler);

            MenuItem removeLeaderPlayer = new MenuItem("Remove player");
            removeLeaderPlayer.setOnAction(removePlayerEventHandler);

            javafx.event.EventHandler<ActionEvent> leaveTeamEventHandler = actionEvent -> {
                try {
                    if (showConfirmationAlert("Do you really want to leave this team?")) {
                        Team team = new Team(((Team) getTreeItem().getValue()).getId());
                        team.removePlayer(Session.getInstance().getPlayer());
                    }
                } catch (SQLException ex) {
                	ErrorAlert.showConnectionAlert();
                    ex.printStackTrace();
                }
                initForShow();
            };

            MenuItem leaveActiveTeamAsLeader = new MenuItem("Leave team");
            leaveActiveTeamAsLeader.setOnAction(leaveTeamEventHandler);

            MenuItem leaveInactiveTeamAsLeader = new MenuItem("Leave team");
            leaveInactiveTeamAsLeader.setOnAction(leaveTeamEventHandler);

            MenuItem leaveTeamAsPlayer = new MenuItem("Leave team");
            leaveTeamAsPlayer.setOnAction(leaveTeamEventHandler);

            activeTeamsContextMenu = new ContextMenu(renameTeam, addPlayer, setInactive, leaveActiveTeamAsLeader, deleteActiveTeam);
            inactiveTeamsContextMenu = new ContextMenu(setActive, leaveInactiveTeamAsLeader, deleteInactiveTeam);
            teamContextMenu = new ContextMenu(leaveTeamAsPlayer);
            playerContextMenu = new ContextMenu(makeLeader, removePlayer);
            leaderContextMenu = new ContextMenu(removeLeader, removeLeaderPlayer);
        }

        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            try {
                if (!empty) {
                    int playerId = Session.getInstance().getPlayer().getId();
                    Object parentValue = getTreeItem().getParent().getValue();
                    Object value = getTreeItem().getValue();
                    setId(null);

                    // players
                    if (getTreeItem().isLeaf()) {
                        if (!parentValue.equals(ROOT_STRING)) {
                            if (value instanceof Player) {
                                Player selectedPlayer = (Player) value;
                                List<Integer> leaderIds = ((Team) parentValue).getLeaderIds();

                                if (leaderIds.contains(selectedPlayer.getId()))
                                    setId("leader-item");

                                if (selectedPlayer.getId() != playerId) {
                                    if (leaderIds.contains(playerId)) {
                                        if (leaderIds.contains(selectedPlayer.getId()))
                                            setContextMenu(leaderContextMenu);
                                        else
                                            setContextMenu(playerContextMenu);
                                    }
                                } else {
                                    setContextMenu(null);
                                }
                            }
                        }
                    } else {
                        // teams
                        List<Integer> leaderIds = new ArrayList<>();
                        if (value instanceof Team)
                            leaderIds = ((Team) value).getLeaderIds();

                        if (leaderIds.contains(playerId)) {
                            if (parentValue.equals(ACTIVE_TEAM_STRING)) {
                                setContextMenu(activeTeamsContextMenu);
                            } else if (parentValue.equals(INACTIVE_TEAM_STRING)) {
                                setContextMenu(inactiveTeamsContextMenu);
                            }
                        } else {
                            if (!value.equals(ACTIVE_TEAM_STRING) && !value.equals(INACTIVE_TEAM_STRING))
                                setContextMenu(teamContextMenu);
                        }
                    }
                }
            } catch (SQLException e) {
            	ErrorAlert.showConnectionAlert();
                e.printStackTrace();
            }
        }

        private boolean showConfirmationAlert(String infoText) {
            ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType deny = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, infoText, confirm, deny);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(MainApplication.instance.getPrimaryStage());

            Optional<ButtonType> result = alert.showAndWait();
            return result.orElse(deny) == confirm;
        }

    }

}
