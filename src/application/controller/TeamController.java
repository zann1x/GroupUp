package application.controller;

import application.Session;
import application.model.Player;
import application.model.Team;
import application.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Comparator;

public class TeamController extends FxmlController {

    @FXML
    private TextField tf_name;
    @FXML
    private ListView<Player> lv_selectedPlayers;
    @FXML
    private ListView<Player> lv_availablePlayers;

    private ObservableList<Player> selectedPlayers;
    private ObservableList<Player> availablePlayers;

    @FXML
    public void initialize() {
        selectedPlayers = FXCollections.observableArrayList();
        availablePlayers = FXCollections.observableArrayList();
    }

    @Override
    public void initForShow() {
        if (lv_availablePlayers != null) {
            try {
                availablePlayers.addAll(Player.getAllPlayers());
                FXCollections.sort(availablePlayers, Comparator.comparing(Player::toString));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            lv_selectedPlayers.setItems(selectedPlayers);
            lv_availablePlayers.setItems(availablePlayers);
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
            Team team = new Team();
            try {
                team.create(tf_name.getText());
                team.addPlayer(Session.getInstance().getPlayer());
                for (int i = 0; i < selectedPlayers.size(); i++) {
                    Player player = selectedPlayers.get(i);
                    team.addPlayer(player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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

}
