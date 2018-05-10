package application.controller;

import application.Session;
import application.model.Player;
import application.model.Team;
import application.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TreeView<Team> tv_teams;

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
        List<Team> teams = new ArrayList<>();
        List<Player> playersInTeam;
        Map<Integer, List<Player>> playerMap = new TreeMap<>();

        for (int id : teamIds) {
            playersInTeam = new ArrayList<>();
            try {
                teams.add(Team.getTeam(id));
                playersInTeam.addAll(Team.getPlayers(id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            playersInTeam.sort(Comparator.comparing(Player::toString));
            playerMap.put(id, playersInTeam);
        }
        teams.sort(Comparator.comparing(Team::toString));

        List<TreeItem> treeItemList = new ArrayList<>();
        for (Team team : teams) {
            playersInTeam = playerMap.get(team.getId());
            TreeItem<String> item = new TreeItem<>(team.getName());
            for (Player player : playersInTeam) {
                TreeItem<String> teamItem = new TreeItem<>(player.getName());
                item.getChildren().add(teamItem);
            }
            treeItemList.add(item);
        }
        TreeItem rootItem = new TreeItem<>("Teams");
        rootItem.getChildren().addAll(treeItemList);

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
                for (int i = 0; i < selectedPlayers.size(); i++) {
                    Player player = selectedPlayers.get(i);
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
}
