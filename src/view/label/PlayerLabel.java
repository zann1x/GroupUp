package view.label;

import javafx.scene.control.Label;
import model.Player;

public class PlayerLabel extends Label {

    private Player player;

    public PlayerLabel(Player player) {
        super(player.getPseudonym());
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
