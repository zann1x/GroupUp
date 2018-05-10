package application.model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int id;
    private String name;
    private List<Integer> teamIds;

    public Player() {
        teamIds = new ArrayList<>();
    }

    public Player(int id, String name) {
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

    public List<Integer> getTeamIds() {
        return teamIds;
    }

    public static List<Integer> getTeamIds(int id) throws SQLException {
        String sql = "SELECT * FROM team_player_mapping WHERE playerId = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Integer> teamIds = new ArrayList<>();
        if (resultSet.first()) {
            do {
                int teamId = resultSet.getInt("teamId");
                teamIds.add(teamId);
            } while (resultSet.next());
        }
        return teamIds;
    }

    public boolean checkCredentials(String username, String password) throws Exception {
        String sql = "SELECT id, name FROM player WHERE name = ? AND password = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        // player names are unique, therefore i'm blindly assuming only one player was selected
        if (resultSet.first()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            assert name.equals(username);
            this.id = id;
            this.name = name;

            teamIds = Player.getTeamIds(id);
        } else {
            return false;
        }

        return true;
    }

    public void setSessionId(String sessionId) throws SQLException {
        String sql = "UPDATE player SET sessionid = ? WHERE id = ?";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, sessionId);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    public static Player getPlayer(int id) throws SQLException {
        String sql = "SELECT id, name FROM player WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        Player player = null;
        if (resultSet.first()) {
            String name = resultSet.getString("name");
            player = new Player(id, name);
        }
        return player;
    }

    public static List<Player> getAllPlayers() throws SQLException {
        String sql = "SELECT * FROM player;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Player> players = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            players.add(new Player(id, name));
        }
        return players;
    }

    public void addTeamId(int id) {
        teamIds.add(id);
    }

    @Override
    public String toString() {
        return name;
    }

}
