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
    public List<Integer> getPlayerIds() throws SQLException {
        sql = "SELECT * FROM team_player_mapping WHERE teamid = ?;";
        return super.getPlayerIds();
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
    public List<Integer> getLeaderIds() throws SQLException {
        sql = "SELECT * FROM team_player_mapping WHERE teamid = ? AND leader = true;";
        return super.getLeaderIds();
    }

    @Override
    public void addLeader(Player player) throws SQLException {
        sql = "UPDATE team_player_mapping SET leader = true WHERE teamid = ? AND playerid = ?;";
        super.addLeader(player);
    }

    @Override
    public void removeLeader(Player player) throws SQLException {
        sql = "UPDATE team_player_mapping SET leader = false WHERE teamid = ? AND playerid = ?;";
        super.removeLeader(player);
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

    public void create(String name, Player player) throws SQLException {
        create(name);
        addPlayer(player, true);
    }

    @Override
    public void addPlayer(Player player, boolean isLeader) throws SQLException {
        sql = "INSERT INTO team_player_mapping (teamid, playerid, leader) VALUE(?, ?, ?);";
        super.addPlayer(player, isLeader);
    }

    public void addPlayers(List<Player> players) throws SQLException {
        for (Player player : players)
            addPlayer(player, false);
    }

    @Override
    public void removePlayer(Player player) throws SQLException {
        sql = "DELETE FROM team_player_mapping WHERE teamid = ? AND playerid = ?;";
        super.removePlayer(player);

        if (getSize() != 0) {
            if (getLeaderIds().isEmpty()) {
                addLeader(new Player(getPlayerIds().get(0)));
            }
        }
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

    @Override
    public void rename(String name) throws SQLException {
        sql = "UPDATE team SET name = ? WHERE id = ?;";
        super.rename(name);
    }

}
