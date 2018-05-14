package application.model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Team extends Party {

    public Team() {
        super();
    }

    public Team(int id, String name) {
        super(id, name);
    }

    public Team(int id, String name, List<Integer> playerIds) {
        super(id, name, playerIds);
    }

    public static Team getTeam(int id) throws SQLException {
        String sql = "SELECT * FROM team WHERE id = ?";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);

        return Team.getTeam(statement);
    }

    public static Team getTeam(String name) throws SQLException {
        String sql = "SELECT * FROM team WHERE name = ?";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);

        return Team.getTeam(statement);
    }

    private static Team getTeam(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        Team team = null;
        if (resultSet.first()) {
            int teamId = resultSet.getInt("id");
            String teamName = resultSet.getString("name");
            List<Integer> playerIds = Party.getPlayerIds(teamId);
            team = new Team(teamId, teamName, playerIds);
        }
        return team;
    }

}
