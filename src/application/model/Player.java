package application.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.MainApplication;

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
}
