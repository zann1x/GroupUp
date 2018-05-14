package view;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import application.MainApplication;
import application.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Party;
import model.Player;

public class AddPlayerPopup {

    private Stage stage;

    public AddPlayerPopup(Party party) {
        VBox root = new VBox();
        ListView<Player> listView = new ListView<>();
        ObservableList<Player> observableList = FXCollections.observableArrayList();

        try {
            List<Player> players = Player.getAllPlayers();
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId())
                    .collect(Collectors.toList());
            players.removeAll(party.getPlayerIds());
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

        root.getChildren().add(listView);

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
