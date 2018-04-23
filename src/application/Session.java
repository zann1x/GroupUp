package application;

import application.model.Player;

import java.util.UUID;

public class Session {

    private static volatile Session instance;
    private static Object mutex = new Object();

    public UUID id;
    private Player player;

    private Session(Player player) {
        this.id = UUID.randomUUID();
        this.player = player;
    }

    public static Session getInstance() throws Exception {
        Session session = instance;
        if (session == null)
            throw new Exception("No session created yet!");
        return session;
    }

    public static void create(Player player) throws Exception {
        Session session = instance;
        if (session == null) {
            synchronized (mutex) {
                instance = new Session(player);
            }
        } else {
            throw new Exception("A Session does already exist!");
        }
    }

    public static void delete() {
        Session session = instance;
        if (session != null) {
            synchronized (mutex) {
                instance = null;
            }
        }
    }

    @Override
    public String toString() {
        return player.getName() + "@" + String.valueOf(id);
    }

}
