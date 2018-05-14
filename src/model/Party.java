package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.MainApplication;

public abstract class Party {

    protected int id;
    protected String name;
    protected List<Integer> playerIds;

    public Party() {
        playerIds = new ArrayList<>();
    }

    public Party(int id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public Party(int id, String name, List<Integer> playerIds) {
        this.id = id;
        this.name = name;
        this.playerIds = playerIds;
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

    public void create(String name) throws SQLException {
        String sql = "INSERT INTO team (name) VALUE(?);";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        statement.executeUpdate();

        ResultSet resultSet = statement.executeQuery("SELECT id FROM team WHERE name = '" + name + "';");
        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = name;
        }
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

    public void removePlayer(Player player) {

    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Integer> getPlayerIds(int id) throws SQLException {
        String sql = "SELECT * FROM team_player_mapping WHERE teamId = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        List<Integer> playerIds = new ArrayList<>();
        if (resultSet.first()) {
            do {
                int playerId = resultSet.getInt("playerId");
                playerIds.add(playerId);
            } while (resultSet.next());
        }
        return playerIds;
    }

    public static List<Player> getPlayers(int id) throws SQLException {
        List<Integer> playerIds = Party.getPlayerIds(id);
        List<Player> players = playerIds.stream().map(p -> {
            try {
                return new Player(p);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return players;
    }
}
