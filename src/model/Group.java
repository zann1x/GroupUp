package model;

import java.sql.SQLException;

public class Group extends Party {

    public Group() {
        super();
    }

    public Group(int id) throws SQLException {
        super(id);
    }

    public Group(String name) throws SQLException {
        super(name);
    }

    private void exitOnLogout() {
        // TODO
    }

}
