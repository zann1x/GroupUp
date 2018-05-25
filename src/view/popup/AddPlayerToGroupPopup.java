package view.popup;

import application.Session;
import javafx.collections.FXCollections;
import model.Group;
import model.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AddPlayerToGroupPopup extends PlayerPopup {

    public AddPlayerToGroupPopup(Group group) {
        lbl_selectedPlayers.setText("Players currently online");

        // collect list items
        try {
            List<Player> players = Player.getAllLoggedInPlayers();
            // remove all players that are already in the team
            List<Player> playersToRemove = new ArrayList<>();
            for (Player player : players) {
                if (player.getId() == Session.getInstance().getPlayer().getId()
                        || group.getPlayerIds().contains(player.getId())) {
                    playersToRemove.add(player);
                }
            }
            players.removeAll(playersToRemove);

            if (players.size() == 0) {
                lbl_selectedPlayers.setText("No players currently online");
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
                    // remove player from his own group first
                    Group g = new Group(player.getGroupId());
                    g.removePlayer(player);

                    group.addPlayer(player, false);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        initialize();
    }

}
