package application;

import java.sql.SQLException;
import java.util.UUID;

import model.Group;
import model.Player;

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

                // add player to an initial group with only him as a member
                Group group = new Group();
                group.create(player);
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
                    // TODO rename group if 'leader' has left
                    group.exitOnLogout();
                } catch (SQLException e) {
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
