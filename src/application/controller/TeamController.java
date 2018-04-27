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

public class TeamController extends FxmlController {

    @FXML
    private TextField tf_name;
    @FXML
    private ListView<Player> lv_selectedPlayers;

    private ObservableList<Player> playerObservableList;

    @FXML
    private void initialize() {
        playerObservableList = FXCollections.observableArrayList();
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

    static int i = 0;

    @FXML
    private void handleAddPlayer() {
        // TODO
        i++;
        Player player = new Player(42, "horst" + i);
        playerObservableList.add(player);
        lv_selectedPlayers.setItems(playerObservableList);
    }

    @FXML
    private void handleRemovePlayer() {
        // TODO
        Player player = lv_selectedPlayers.getSelectionModel().getSelectedItem();
        playerObservableList.remove(player);
    }
}
