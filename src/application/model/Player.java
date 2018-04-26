package application.model;

import application.MainApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Player {

    private int id;
    private String name;
    private List<Integer> teamIds;

    public Player() {

    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean checkCredentials(String username, String password) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement("SELECT id, name FROM player WHERE name = ? AND password = ?;");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        // player names are unique, therefore i'm blindly assuming only one player was selected
        if (resultSet.first()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            assert name.equals(username);
            this.id = id;
            this.name = name;
        } else {
            return false;
        }
        
        return true;
    }

    public void setSessionId(String sessionId) throws SQLException {
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement("UPDATE player SET sessionid = ? WHERE id = ?");
        preparedStatement.setString(1, sessionId);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

}
