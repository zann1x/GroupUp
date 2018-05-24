package view;

import application.Session;
import javafx.collections.FXCollections;
import model.Party;
import model.Player;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddPlayerToTeamPopup extends AddPlayerPopup {

    public AddPlayerToTeamPopup(Party party) {
        lbl_selectedPlayers.setText("Selectable players");

        // collect list items
        try {
            List<Player> players = Player.getAllPlayers();
            // remove all players that are already in the team
            players = players.stream()
                    .filter(p -> p.getId() != Session.getInstance().getPlayer().getId()
                            && !party.getPlayerIds().contains(p.getId()))
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
                    party.addPlayer(player);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        initialize();
    }

}
