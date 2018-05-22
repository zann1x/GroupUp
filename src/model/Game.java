package model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private int id;
    private String name;

    public Game(int id) throws SQLException {
        this.id = id;
        getDataFromId();
    }

    public Game(String name) throws SQLException {
        this.name = name;
        getDataFromName();
    }

    private void getDataFromId() throws SQLException {
        String sql = "SELECT * FROM game WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        getData(statement);
    }

    private void getDataFromName() throws SQLException {
        String sql = "SELECT * FROM game WHERE name = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        getData(statement);
    }

    private void getData(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Game> getAllGames() throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM game;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            games.add(new Game(id));
        }
        return games;
    }

}
