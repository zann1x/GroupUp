package application.model;

import java.util.List;

public abstract class Party {

    protected int id;
    protected String name;
    protected List<Integer> playerIds;

    abstract void create();
    abstract void delete();
    abstract void addPlayer(int playerId);
    abstract void removePlayer(int playerId);

}
