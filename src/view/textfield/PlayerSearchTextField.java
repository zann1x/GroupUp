package view.textfield;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.Player;

import java.util.stream.Collectors;

public class PlayerSearchTextField extends TextField {

    public void search(String playerName, ObservableList<Player> filteredPlayers, ObservableList<Player> allPlayers) {
        if (playerName.length() >= 3) {
            filteredPlayers.setAll(
                    allPlayers.stream()
                            .filter(player -> player.getPseudonym().toLowerCase().contains(playerName.toLowerCase()))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList))
            );
        } else {
            filteredPlayers.setAll(allPlayers);
        }
    }

}
