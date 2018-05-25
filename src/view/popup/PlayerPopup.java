package view.popup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Player;

public class PlayerPopup extends VBoxPopup {

    protected Label lbl_selectedPlayers;
    protected TextField tf_searchPlayer;
    protected ListView<Player> lv_players;

    protected ObservableList<Player> playerObservableList;
    protected EventHandler<MouseEvent> doubleClickEventHandler;

    public PlayerPopup() {
        lbl_selectedPlayers = new Label();
        tf_searchPlayer = new TextField();
        lv_players = new ListView<>();
        playerObservableList = FXCollections.observableArrayList();

        // add elements to vbox
        addChild(lbl_selectedPlayers);
        addChild(tf_searchPlayer);
        addChild(lv_players);
    }

    protected void initialize() {
        // text field for searching players
        // TODO enable searching in player list
        tf_searchPlayer.setPromptText("Enter a pseudonym here");
        tf_searchPlayer.setDisable(true);

        // setup of the list view
        lv_players.setItems(playerObservableList);
        lv_players.setPrefWidth(300.0d);
        lv_players.setPrefHeight(300.0d);
        lv_players.setOnMouseClicked(doubleClickEventHandler);
    }

}
