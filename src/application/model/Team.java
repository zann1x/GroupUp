package application.model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Team extends Party {

    public Team() {
        super();
    }

    public Team(int id, String name) {
        super(id, name);
    }

    public static Team getTeam(int id) throws SQLException {
        String sql = "SELECT * FROM team WHERE id = ?";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        Team team = null;
        if (resultSet.first()) {
            int teamId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            team = new Team(teamId, name);
        }
        return team;
    }

    public static List<Player> getPlayers(int id) throws SQLException {
        String sql = "SELECT * FROM team_player_mapping WHERE teamId = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        List<Player> players = new ArrayList<>();
        if (resultSet.first()) {
            do {
                int playerId = resultSet.getInt("playerId");
                Player player = Player.getPlayer(playerId);
                players.add(player);
            } while (resultSet.next());
        }
        return players;
    }

}
