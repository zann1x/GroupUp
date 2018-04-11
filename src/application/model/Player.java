package application.model;

import java.util.List;

public class Player {

    private int id;
    private int name;
    private List<Integer> teamIds;

    public Player(int id) {
        this.id = id;
        // TODO check if player exists
    }

}
