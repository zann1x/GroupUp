package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Team extends Party {

    protected boolean active;

    public Team() {
        super();
    }

    public Team(int id) throws SQLException {
        super(id);
    }

    @Override
    protected void getData() throws SQLException {
        sql = "SELECT * FROM team WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
            this.active = resultSet.getBoolean("isactive");
        }

        sql = "SELECT * FROM team_player_mapping WHERE teamid = ?;";
        super.collectPlayerIds();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) throws SQLException {
        this.active = active;

        sql = "UPDATE team SET isActive = ? WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setBoolean(1, active);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    @Override
    protected void create(String name) throws SQLException {
        sql = "INSERT INTO team (name) VALUE(?);";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        statement.executeUpdate();

        sql = "SELECT * FROM team WHERE name = ?;";
        super.create(name);
    }

    @Override
    public void addPlayer(Player player) throws SQLException {
        sql = "INSERT INTO team_player_mapping (teamid, playerid) VALUE(?, ?);";
        super.addPlayer(player);

        player.addTeamId(id);
        playerIds.add(player.getId());
    }

    @Override
    public void removePlayer(Player player) throws SQLException {
        sql = "DELETE FROM team_player_mapping WHERE teamid = ? AND playerid = ?;";
        super.removePlayer(player);

        player.removeTeamId(id);
    }

    private void removeAllPlayers() throws SQLException {
        List<Player> players = getPlayers();
        for (Player player : players)
            removePlayer(player);
    }

    @Override
    public void delete() throws SQLException {
        removeAllPlayers();

        sql = "DELETE FROM team WHERE id = ?;";
        super.delete();
    }

}
