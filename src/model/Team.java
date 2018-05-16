package model;

import application.MainApplication;

import java.sql.PreparedStatement;
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

    @Override
    public void removePlayer(Player player) throws SQLException {
        super.removePlayer(player);
        if (playerIds.isEmpty()) {
            String sql = "DELETE FROM team WHERE id = ?;";
            PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
