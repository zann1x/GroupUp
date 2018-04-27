package application;

import application.model.Player;

import java.sql.SQLException;
import java.util.UUID;

public class Session {

    private static volatile Session instance;
    private static final Object mutex = new Object();

    public UUID id;
    private Player player;

    private Session(Player player) {
        this.id = UUID.randomUUID();
        this.player = player;
    }

    public static Session getInstance() {
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    public static void create(Player player) throws Exception {
        Session session = instance;
        if (session == null) {
            synchronized (mutex) {
                instance = new Session(player);
                player.setSessionId(instance.id.toString());
            }
        } else {
            throw new Exception("A Session does already exist!");
        }
    }

    public void delete() {
        Session session = instance;
        if (session != null) {
            synchronized (mutex) {
                instance = null;
                try {
                    player.setSessionId(null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return player.getName() + "@" + String.valueOf(id);
    }

}
