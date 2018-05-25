package view.popup;

import application.Session;
import javafx.collections.FXCollections;
import model.Group;
import model.Player;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RemovePlayerFromGroupPopup extends PlayerPopup {

    public RemovePlayerFromGroupPopup(Group group) {
        lbl_selectedPlayers.setText("Selectable players");

        // collect list items
        try {
            List<Player> players = group.getPlayers();
            // remove all players that are already in the team
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId())
                    .collect(Collectors.toList());

            if (players.size() == 0) {
                lbl_selectedPlayers.setText("No players selectable");
            }
            playerObservableList.addAll(players);
            FXCollections.sort(playerObservableList, Comparator.comparing(Player::toString));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add double click to list view
        doubleClickEventHandler = event -> {
            if (event.getClickCount() >= 2) {
                Player player = lv_players.getSelectionModel().getSelectedItem();
                try {
                    // TODO change group name if 'leader' was removed / has left
                    group.removePlayer(player);

                    // add removed player to his own group again
                    Group g = new Group();
                    g.create(player.getPseudonym(), player);

                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        initialize();
    }

}
