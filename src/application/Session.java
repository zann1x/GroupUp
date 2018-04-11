package application;

import application.model.Player;

public class Session {

    private static volatile Session instance;
    private static Object mutex = new Object();

    public int id;
    private Player player;

    private Session(int playerId) {
        player = new Player(playerId);
    }

    public static Session getInstance() throws Exception {
        Session session = instance;
        if (session == null)
            throw new Exception("No session created yet!");
        return session;
    }

    public static void create(int playerId) throws Exception {
        Session session = instance;
        if (session == null) {
            synchronized (mutex) {
                instance = new Session(playerId);
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
        return "sid" + String.valueOf(id);
    }

}
