package application.model;

import java.util.List;

public abstract class Party {

    protected int id;
    protected String name;
    protected List<Integer> playerIds;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void create();
    public abstract void delete();
    public abstract void addPlayer(int playerId);
    public abstract void removePlayer(int playerId);

}
