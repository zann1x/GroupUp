package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Party {

    protected String sql;

    protected int id;
    protected String name;
    protected List<Integer> playerIds;

    public Party() {
        playerIds = new ArrayList<>();
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

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    protected abstract void getData() throws SQLException;

    protected void collectPlayerIds() throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            do {
                int playerId = resultSet.getInt("playerid");
                playerIds.add(playerId);
            } while (resultSet.next());
        }
    }

    protected void create(String name) throws SQLException {
        // TODO improve this method as soon as i find out how to get the table name of a query

        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first())
            this.id = resultSet.getInt("id");

        getData();
    }

    public void create(String name, Player player) throws SQLException {
        create(name);
        addPlayer(player);
    }

    public void addPlayer(Player player) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();
    }

    public void addPlayers(List<Player> players) throws SQLException {
        for (Player player : players)
            addPlayer(player);
    }

    public void removePlayer(Player player) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();

        playerIds.remove(Integer.valueOf(player.getId()));
        if (playerIds.isEmpty()) {
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
        List<Player> players = new ArrayList<>();
        for (int id : playerIds) {
            players.add(new Player(id));
        }
        return players;
    }

}
