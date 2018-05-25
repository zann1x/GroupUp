package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO add 'leader' functionality to group and team
public abstract class Party {

    protected String sql;

    protected int id;
    protected String name;

    public Party() {
    }

    public Party(int id) throws SQLException {
        this();
        this.id = id;
        getData();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected abstract void getData() throws SQLException;

    public List<Integer> getPlayerIds() throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        List<Integer> playerIds = new ArrayList<>();
        if (resultSet.first()) {
            do {
                int playerId = resultSet.getInt("playerid");
                playerIds.add(playerId);
            } while (resultSet.next());
        }
        return playerIds;
    }

    public List<Integer> getLeaderIds() throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        List<Integer> leaders = new ArrayList<>();
        while (resultSet.next()){
            int playerId = resultSet.getInt("playerid");
            leaders.add(playerId);
        }
        return leaders;
    }

    public void addLeader(Player player) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();
    }

    public void removeLeader(Player player) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();
    }

    protected void create(String name) throws SQLException {
        // TODO improve this method as soon as i find out how to get the table name of a query

        // sql = "SELECT * FROM xxxx WHERE name = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first())
            this.id = resultSet.getInt("id");

        getData();
    }

    public void create(String name, Player player) throws SQLException {
        create(name);
        addPlayer(player, true);
    }

    public void addPlayer(Player player, boolean isLeader) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.setBoolean(3, isLeader);
        statement.executeUpdate();
    }

    public void addPlayers(List<Player> players) throws SQLException {
        for (Player player : players)
            addPlayer(player, false);
    }

    public void removePlayer(Player player) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();

        if (getPlayerIds().isEmpty()) {
            delete();
        }
    }

    public void delete() throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Player> getPlayers() throws SQLException {
        List<Integer> playerIds = getPlayerIds();
        List<Player> players = new ArrayList<>();

        for (int id : playerIds) {
            players.add(new Player(id));
        }
        return players;
    }

}
