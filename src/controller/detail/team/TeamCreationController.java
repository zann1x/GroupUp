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
    private ListView<Player> lv_selectedPlayers;
    @FXML
    private ListView<Player> lv_availablePlayers;
    @FXML
    private Label lbl_createSuccess;

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

    @Override
    public void initForShow() {
        if (teamCreationRoot != null) {
            tf_name.setText("");
            availablePlayers.clear();
            selectedPlayers.clear();
            lbl_createSuccess.setText("");

            initTeamCreationView();
        }
    }

    @FXML
    private void handleSubmit() {
        if (mandatoryFieldsFilled()) {
            boolean createSuccess;
            Team team = new Team();
            try {
                team.create(tf_name.getText(), Session.getInstance().getPlayer());
                team.addPlayers(selectedPlayers);
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
        if (lv_availablePlayers.getSelectionModel().getSelectedItem() != null) {
            selectedPlayers.add(lv_availablePlayers.getSelectionModel().getSelectedItem());
            availablePlayers.remove(lv_availablePlayers.getSelectionModel().getSelectedIndex());
            FXCollections.sort(selectedPlayers, Comparator.comparing(Player::toString));
        }
    }

    @FXML
    private void handleRemovePlayer() {
        if (lv_selectedPlayers.getSelectionModel().getSelectedItem() != null) {
            availablePlayers.add(lv_selectedPlayers.getSelectionModel().getSelectedItem());
            selectedPlayers.remove(lv_selectedPlayers.getSelectionModel().getSelectedIndex());
            FXCollections.sort(availablePlayers, Comparator.comparing(Player::toString));
        }
    }

}
