package view;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import application.MainApplication;
import application.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Party;
import model.Player;

public class AddPlayerPopup {

    private Stage stage;

    public AddPlayerPopup(Party party) {
        Label lbl_selectedPlayers = new Label("Selectable Players");
        ListView<Player> listView = new ListView<>();
        ObservableList<Player> observableList = FXCollections.observableArrayList();

        try {
            List<Player> players = Player.getAllPlayers();
            // remove all players that are already in the team
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId()
                            && !party.getPlayerIds().contains(p.getId()))
                    .collect(Collectors.toList());

            if (players.size() == 0)
                lbl_selectedPlayers.setText("No players selectable");
            observableList.addAll(players);
            FXCollections.sort(observableList, Comparator.comparing(Player::toString));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listView.setItems(observableList);
        listView.setPrefWidth(300.0d);
        listView.setPrefHeight(400.0d);
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Player player = listView.getSelectionModel().getSelectedItem();
                try {
                    party.addPlayer(player);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox root = new VBox(5.0d);
        root.setPadding(new Insets(5.0d));
        root.getChildren().addAll(lbl_selectedPlayers, listView);

        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApplication.instance.getPrimaryStage());
    }

    public void show() {
        stage.show();
    }
}
