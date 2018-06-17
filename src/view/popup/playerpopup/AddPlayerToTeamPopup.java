package view.popup.playerpopup;

import application.Session;
import model.Player;
import model.Team;
import view.alert.ErrorAlert;

import java.sql.SQLException;
import java.util.List;

public class AddPlayerToTeamPopup extends PlayerPopup {

    public AddPlayerToTeamPopup(Team team) {
        lbl_selectedPlayers.setText("Selectable players");

        // collect list items
        try {
            List<Player> players = Player.getAllPlayers();
            // add only those players that aren't already in the team
            for (Player player : players) {
                if (player.getId() != Session.getInstance().getPlayer().getId()
                        && !team.getPlayerIds().contains(player.getId())) {
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
                    team.addPlayer(player, false);
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
