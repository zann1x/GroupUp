package view.popup.playerpopup;

import application.Session;
import model.Group;
import model.Player;
import view.alert.ErrorAlert;

import java.sql.SQLException;
import java.util.List;

public class AddPlayerToGroupPopup extends PlayerPopup {

    public AddPlayerToGroupPopup(Group group) {
        lbl_selectedPlayers.setText("Players currently online");

        // collect list items
        try {
            List<Player> players = Player.getAllLoggedInPlayers();
            // add only those players that aren't already in the team
            for (Player player : players) {
                if (player.getId() != Session.getInstance().getPlayer().getId()
                        && !group.getPlayerIds().contains(player.getId())) {
                    allAvailablePlayers.add(player);
                }
            }
        } catch (SQLException e) {
        	ErrorAlert.showConnectionAlert();
            e.printStackTrace();
        }

        // add double click to list view
        doubleClickEventHandler = event -> {
            if (event.getClickCount() >= 2) {
                Player player = lv_players.getSelectionModel().getSelectedItem();
                try {
                    group.sendInvite(player);
                    stage.close();
                } catch (SQLException e) {
                	ErrorAlert.showConnectionAlert();
                    e.printStackTrace();
                }
            }
        };

        setupElements();
    }
}
