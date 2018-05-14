package application.controller;

import application.Session;
import application.model.Party;
import application.model.Team;
import application.util.Player;
import application.util.navigation.ViewNavigator;
import application.view.AddPlayerPopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TeamController extends FxmlController {

    @FXML
    private BorderPane teamCreationRoot;
    @FXML
    private BorderPane ownTeamsRoot;

    @FXML
    private TextField tf_name;
    @FXML
    private ListView<Player> lv_selectedPlayers;
    @FXML
    private ListView<Player> lv_availablePlayers;
    @FXML
    private Label lbl_createSuccess;

    @FXML
    private TreeView<String> tv_teams;

    private ObservableList<Player> selectedPlayers;
    private ObservableList<Player> availablePlayers;

    @FXML
    public void initialize() {
        selectedPlayers = FXCollections.observableArrayList();
        availablePlayers = FXCollections.observableArrayList();
    }

    private void initTeamCreationView() {
        try {
            List<Player> players = Player.getAllPlayers();
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId())
                    .collect(Collectors.toList());
            availablePlayers.addAll(players);
            FXCollections.sort(availablePlayers, Comparator.comparing(Player::toString));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lv_selectedPlayers.setItems(selectedPlayers);
        lv_availablePlayers.setItems(availablePlayers);
    }

    private void initOwnTeamsView() {
        List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();
        List<Team> ownTeams = new ArrayList<>();
        List<Player> playersInTeam;
        Map<Integer, List<Player>> playerMap = new TreeMap<>();

        // collect own teams and all players in them
        for (int id : teamIds) {
            // initialize at each iteration, so every team gets its own list of players
            playersInTeam = new ArrayList<>();
            try {
                ownTeams.add(Team.getTeam(id));
                playersInTeam = Party.getPlayers(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            playersInTeam.sort(Comparator.comparing(Player::toString));
            playerMap.put(id, playersInTeam);
        }
        ownTeams.sort(Comparator.comparing(Team::toString));

        // populate tree view with all collected teams
        TreeItem<String> rootItem = new TreeItem<>("Teams");
        for (Team team : ownTeams) {
            playersInTeam = playerMap.get(team.getId());
            TreeItem<String> teamItem = new TreeItem<>(team.getName());
            for (Player player : playersInTeam) {
                TreeItem<String> playerItem = new TreeItem<>(player.getName());
                teamItem.getChildren().add(playerItem);
            }
            rootItem.getChildren().add(teamItem);
        }

        tv_teams.setCellFactory(param -> new CustomTreeCell());
        tv_teams.setRoot(rootItem);
        tv_teams.setShowRoot(false);
    }

    @Override
    public void initForShow() {
        if (teamCreationRoot != null) {
            tf_name.setText("");
            availablePlayers.clear();
            selectedPlayers.clear();
            lbl_createSuccess.setText("");

            initTeamCreationView();
        }

        if (ownTeamsRoot != null) {
            initOwnTeamsView();
        }
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.TEAM_DETAIL, "Team detail");
    }

    @FXML
    private void createNewTeam() {
        super.showDetail(ViewNavigator.NodeName.TEAM_CREATION, "Team creation");
    }

    @FXML
    private void handleSubmit() {
        if (mandatoryFieldsFilled()) {
            boolean createSuccess;
            Team team = new Team();
            try {
                team.create(tf_name.getText());
                team.addPlayer(Session.getInstance().getPlayer());
                for (Player player : selectedPlayers) {
                    team.addPlayer(player);
                }
                createSuccess = true;
            } catch (SQLException e) {
                createSuccess = false;
                e.printStackTrace();
            }

            initForShow();
            if (createSuccess) {
                lbl_createSuccess.setTextFill(Paint.valueOf("GREEN"));
                lbl_createSuccess.setText("Team created successfully!");
            } else {
                lbl_createSuccess.setTextFill(Paint.valueOf("RED"));
                lbl_createSuccess.setText("Team creation unsuccessful!");
            }
        }
    }

    private boolean mandatoryFieldsFilled() {
        boolean isAllFilled = true;
        if (tf_name.getText().equals(""))
            isAllFilled = false;

        if (!isAllFilled) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Not all mandatory fields are filled!");
            alert.showAndWait();
        }
        return isAllFilled;
    }

    @FXML
    private void handleAddPlayer() {
        if (!lv_availablePlayers.getSelectionModel().getSelectedItem().equals("")) {
            selectedPlayers.add(lv_availablePlayers.getSelectionModel().getSelectedItem());
            availablePlayers.remove(lv_availablePlayers.getSelectionModel().getSelectedIndex());
            FXCollections.sort(selectedPlayers, Comparator.comparing(Player::toString));
        }
    }

    @FXML
    private void handleRemovePlayer() {
        if (!lv_selectedPlayers.getSelectionModel().getSelectedItem().equals("")) {
            availablePlayers.add(lv_selectedPlayers.getSelectionModel().getSelectedItem());
            selectedPlayers.remove(lv_selectedPlayers.getSelectionModel().getSelectedIndex());
            FXCollections.sort(availablePlayers, Comparator.comparing(Player::toString));
        }
    }

    @FXML
    private void showTeams() {
        super.showDetail(ViewNavigator.NodeName.OWN_TEAMS, "Teams");
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
                    Team team = Team.getTeam(getTreeItem().getValue());
                    new AddPlayerPopup(team).show();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            teamContextMenu = new ContextMenu(rootItem);

            MenuItem leafItem = new MenuItem("Remove player");
            leafItem.setOnAction(e -> {
                System.out.println("remove");
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
