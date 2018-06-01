package view.popup.playerpopup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Player;
import view.popup.VBoxPopup;
import view.textfield.PlayerSearchTextField;

import java.util.Comparator;

public class PlayerPopup extends VBoxPopup {

    protected Label lbl_selectedPlayers;
    protected PlayerSearchTextField tf_playerSearch;
    protected ListView<Player> lv_players;

    private ObservableList<Player> availablePlayers;
    protected ObservableList<Player> allAvailablePlayers;
    protected EventHandler<MouseEvent> doubleClickEventHandler;

    public PlayerPopup() {
        lbl_selectedPlayers = new Label();
        tf_playerSearch = new PlayerSearchTextField();
        lv_players = new ListView<>();
        availablePlayers = FXCollections.observableArrayList();
        allAvailablePlayers = FXCollections.observableArrayList();

        // add elements to vbox
        addChild(lbl_selectedPlayers);
        addChild(tf_playerSearch);
        addChild(lv_players);
    }

    protected void setupElements() {
        if (allAvailablePlayers.size() == 0) {
            lbl_selectedPlayers.setText("No players selectable");
            tf_playerSearch.setDisable(true);
        } else {
            lbl_selectedPlayers.setText("Selectable players");
            tf_playerSearch.setDisable(false);
        }

        FXCollections.sort(allAvailablePlayers, Comparator.comparing(Player::toString));
        availablePlayers.setAll(allAvailablePlayers);

        // text field for searching players
        tf_playerSearch.setPromptText("Enter a pseudonym here");
        tf_playerSearch.setOnKeyReleased(event -> tf_playerSearch.search(tf_playerSearch.getText(), availablePlayers, allAvailablePlayers));

        // setup of the list view
        lv_players.setItems(availablePlayers);
        lv_players.setPrefWidth(300.0d);
        lv_players.setPrefHeight(300.0d);
        lv_players.setOnMouseClicked(doubleClickEventHandler);
    }

}
