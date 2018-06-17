package application;

import model.Group;
import model.Player;

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
                if (player.getSessionId() != null)
                    throw new Exception("A Session does already exist!");

                // add player to an initial group with only him as a member
                Group group = new Group();
                group.create(player);

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
                try {
                    player.setSessionId(null);

                    // remove player from his current group
                    Group group = new Group(player.getGroupId());
                    group.exitOnLogout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                instance = null;
            }
        }
    }

    @Override
    public String toString() {
        return player.getPseudonym() + "@" + String.valueOf(id);
    }

}
