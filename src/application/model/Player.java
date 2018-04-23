package application.model;

import java.util.List;

public class Player {

    private int id;
    private String name;
    private List<Integer> teamIds;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
