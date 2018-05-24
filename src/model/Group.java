package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Group extends Party {

    public Group() {
        super();
    }

    public Group(int id) throws SQLException {
        super(id);
    }

    @Override
    protected void getData() throws SQLException {
        sql = "SELECT * FROM group WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
        }

        sql = "SELECT * FROM group_player_mapping WHERE groupid = ?;";
        super.collectPlayerIds();
    }

    @Override
    protected void create(String name) throws SQLException {
        sql = "INSERT INTO group (name) VALUE(?);";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        statement.executeUpdate();

        sql = "SELECT * FROM group WHERE name = ?;";
        super.create(name);
    }

    @Override
    public void addPlayer(Player player) throws SQLException {
        sql = "INSERT INTO group_player_mapping (groupid, playerid) VALUE(?, ?);";
        super.addPlayer(player);

        player.addToGroup(id);
        playerIds.add(player.getId());
    }

    @Override
    public void removePlayer(Player player) throws SQLException {
        sql = "DELETE FROM group_player_mapping WHERE groupid = ? AND playerid = ?;";
        super.removePlayer(player);

        player.removeFromGroup();
    }

    @Override
    public void delete() throws SQLException {
        sql = "DELETE FROM group WHERE id = ?;";
        super.delete();
    }

}
