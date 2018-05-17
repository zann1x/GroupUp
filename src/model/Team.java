package model;

import java.sql.SQLException;

public class Team extends Party {

    public Team() {
        super();
    }

    public Team(int id) throws SQLException {
        super(id);
    }

    public Team(String name) throws SQLException {
        super(name);
    }

}
