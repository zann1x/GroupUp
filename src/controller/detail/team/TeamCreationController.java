package controller.detail.team;

import application.Session;
import controller.FxmlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import model.Player;
import model.Team;
import view.alert.ErrorAlert;
import view.textfield.PlayerSearchTextField;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamCreationController extends FxmlController {

    @FXML
    private BorderPane teamCreationRoot;

    @FXML
    private TextField tf_name;
    @FXML
    private PlayerSearchTextField tf_playerSearch;
    @FXML
    private ListView<Player> lv_selectedPlayers;
    @FXML
    private ListView<Player> lv_availablePlayers;
    @FXML
    private Label lbl_createSuccess;

    private ObservableList<Player> selectedPlayers;
    private ObservableList<Player> availablePlayers;
    private ObservableList<Player> allAvailablePlayers;

    @FXML
    public void initialize() {
        selectedPlayers = FXCollections.observableArrayList();
        availablePlayers = FXCollections.observableArrayList();
        allAvailablePlayers = FXCollections.observableArrayList();
    }

    private void initTeamCreationView() {
        try {
            List<Player> players = Player.getAllPlayers();
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId())
                    .collect(Collectors.toList());
            allAvailablePlayers.addAll(players);
            FXCollections.sort(allAvailablePlayers, Comparator.comparing(Player::toString));
            availablePlayers.setAll(allAvailablePlayers);
        } catch (SQLException e) {
        	ErrorAlert.showConnectionAlert();
            e.printStackTrace();
        }
        lv_selectedPlayers.setItems(selectedPlayers);
        lv_availablePlayers.setItems(availablePlayers);
    }

    @Override
    public void initForShow() {
        super.initForShow();
        if (teamCreationRoot != null) {
            tf_name.setText("");
            availablePlayers.clear();
            allAvailablePlayers.clear();
            selectedPlayers.clear();
            lbl_createSuccess.setText("");

            initTeamCreationView();
        }
    }

    @FXML
    private void handleSubmit() {
        if (mandatoryFieldsFilled()) {
            Team team = new Team();
            String teamName = tf_name.getText();
            if (teamName.length() < 3) {
                lbl_createSuccess.setTextFill(Paint.valueOf("RED"));
                lbl_createSuccess.setText("Team name too short (min. 3)!");
            } else {
                try {
                    team.create(teamName, Session.getInstance().getPlayer());
                    team.addPlayers(selectedPlayers);

                    initForShow();
                    lbl_createSuccess.setTextFill(Paint.valueOf("GREEN"));
                    lbl_createSuccess.setText("Team created successfully!");
                } catch (SQLException e) {
                	lbl_createSuccess.setText("");
                	ErrorAlert.showConnectionAlert();
                    e.printStackTrace();
                }
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
        if (lv_availablePlayers.getSelectionModel().getSelectedItem() != null) {
            Player selectedPlayer = lv_availablePlayers.getSelectionModel().getSelectedItem();
            selectedPlayers.add(selectedPlayer);
            availablePlayers.remove(selectedPlayer);
            allAvailablePlayers.remove(selectedPlayer);
            FXCollections.sort(selectedPlayers, Comparator.comparing(Player::getPseudonym));
        }
    }

    @FXML
    private void handleRemovePlayer() {
        if (lv_selectedPlayers.getSelectionModel().getSelectedItem() != null) {
            Player selectedPlayer = lv_selectedPlayers.getSelectionModel().getSelectedItem();
            availablePlayers.add(selectedPlayer);
            allAvailablePlayers.add(selectedPlayer);
            selectedPlayers.remove(selectedPlayer);
            FXCollections.sort(availablePlayers, Comparator.comparing(Player::getPseudonym));
        }
    }

    @FXML
    private void onPlayerSearch() {
        tf_playerSearch.search(tf_playerSearch.getText(), availablePlayers, allAvailablePlayers);
        FXCollections.sort(availablePlayers, Comparator.comparing(Player::getPseudonym));
    }

}
