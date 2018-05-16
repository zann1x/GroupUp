package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Party {

    protected int id;
    protected String name;
    protected boolean isActive;
    protected List<Integer> playerIds;

    public Party() {
        playerIds = new ArrayList<>();
    }

    public Party(int id) throws SQLException {
        this();
        this.id = id;
        getDataFromId();
    }

    public Party(String name) throws SQLException {
        this();
        this.name = name;
        getDataFromName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    private void getDataFromId() throws SQLException {
        String sql = "SELECT * FROM team WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        getData(statement);
    }

    private void getDataFromName() throws SQLException {
        String sql = "SELECT * FROM team WHERE name = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        getData(statement);
    }

    private void getData(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
            this.isActive = resultSet.getBoolean("isActive");
        }

        String sql = "SELECT * FROM team_player_mapping WHERE teamId = ?;";
        statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();

        if (resultSet.first()) {
            do {
                int playerId = resultSet.getInt("playerId");
                playerIds.add(playerId);
            } while (resultSet.next());
        }
    }

    private void create(String name) throws SQLException {
        String sql = "INSERT INTO team (name) VALUE(?);";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        statement.executeUpdate();

        this.name = name;
        getDataFromName();
    }

    public void create(String name, Player player) throws SQLException {
        create(name);
        addPlayer(player);
    }

    public void delete() {

    }

    public void addPlayer(Player player) throws SQLException {
        int playerId = player.getId();

        String sql = "INSERT INTO team_player_mapping (teamId, playerId) VALUE(?, ?);";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, playerId);
        statement.executeUpdate();

        player.addTeamId(id);
        playerIds.add(playerId);
    }

    public void addPlayers(List<Player> players) throws SQLException {
        for (Player player : players)
            addPlayer(player);
    }

    public void removePlayer(Player player) throws SQLException {
        int playerId = player.getId();

        String sql = "DELETE FROM team_player_mapping WHERE teamId = ? AND playerId = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, playerId);
        statement.executeUpdate();

        player.removeTeamId(id);
        playerIds.remove(Integer.valueOf(playerId));
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Player> getPlayers() {
        return playerIds.stream().map(p -> {
            try {
                return new Player(p);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

}
