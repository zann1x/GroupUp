package view.popup.playerpopup;

import application.Session;
import model.Group;
import model.Player;

import java.sql.SQLException;
import java.util.List;

public class RemovePlayerFromGroupPopup extends PlayerPopup {

    public RemovePlayerFromGroupPopup(Group group) {
        lbl_selectedPlayers.setText("Selectable players");

        // collect list items
        try {
            List<Player> players = group.getPlayers();
            // add only those players that aren't already in the team
            for (Player player : players) {
                if (player.getId() != Session.getInstance().getPlayer().getId() && !group.getLeaderIds().contains(player.getId())) {
                    allAvailablePlayers.add(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add double click to list view
        doubleClickEventHandler = event -> {
            if (event.getClickCount() >= 2) {
                Player player = lv_players.getSelectionModel().getSelectedItem();
                try {
                    group.removePlayer(player);

                    // add removed player to his own group again
                    Group g = new Group();
                    g.create(player);

                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        setupElements();
    }

}
