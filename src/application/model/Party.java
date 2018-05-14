package application.model;

import application.MainApplication;
import application.util.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

}
