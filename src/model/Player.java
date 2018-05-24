package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.MainApplication;

public class Player {

    private int id;
    private String forename;
    private String surname;
    private String pseudonym;
    private String email;
    private int groupId;
    private List<Integer> teamIds;

    public Player() {
        teamIds = new ArrayList<>();
    }

    public Player(int id) throws SQLException {
        this();
        this.id = id;
        this.getData();
    }

    public int getId() {
        return id;
    }
    
    public String getForename(){
    	return forename;
    }
    
    public String getSurname(){
    	return surname;
    }
    
    public String getPseudonym(){
    	return pseudonym;
    }

    public String getEmail() {
        return email;
    }

    public int getGroupId() {
        return groupId;
    }

    public List<Integer> getTeamIds() throws SQLException {
        String sql = "SELECT * FROM team_player_mapping WHERE playerId = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        teamIds.clear();
        if (rs.first()) {
            do {
                int teamId = rs.getInt("teamId");
                teamIds.add(teamId);
            } while (rs.next());
        }
    	return teamIds;
    }

    private void getData() throws SQLException {
        String sql = "SELECT * FROM player WHERE id = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        //player names are unique, therefore i'm blindly assuming only one player was selected
        while(rs.next()){
            this.id = rs.getInt("id");
            this.forename = rs.getString("forename");
            this.surname = rs.getString("surname");
            this.pseudonym = rs.getString("pseudonym");
            this.email = rs.getString("email");
        }
        getTeamIds();
    }

    public boolean checkCredentials(String pseudonym, String password) throws Exception {
        String sql = "SELECT id, pseudonym FROM player WHERE pseudonym = ? AND password = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, pseudonym);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        // player names are unique, therefore i'm blindly assuming only one player was selected
        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.getData();
            return true;
        } else {
            return false;
        }
    }

    public void setSessionId(String sessionId) throws SQLException {
        String sql = "UPDATE player SET sessionid = ? WHERE id = ?";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, sessionId);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    public static List<Player> getAllPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT id FROM player;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            players.add(new Player(id));
        }
        return players;
    }

    public static List<Player> getAllLoggedInPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT id FROM player WHERE sessionid IS NOT NULL;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            players.add(new Player(id));
        }
        return players;
    }
    
    public void updatePlayer(String forename, String surname, String pseudonym, String password, String email) throws SQLException{
    	String sql = "UPDATE player SET forename = ?, surname = ?, pseudonym = ?, password = ?, email = ? WHERE id = ?";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, forename);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, pseudonym);
        preparedStatement.setString(4, password);
        preparedStatement.setString(5, email);
        preparedStatement.setInt(6, id);
        preparedStatement.executeUpdate();
    }
    
    public void updatePlayer(String forename, String surname, String pseudonym, String email) throws SQLException{
    	String sql = "UPDATE player SET forename = ?, surname = ?, pseudonym = ?, email = ? WHERE id = ?";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, forename);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, pseudonym);
        preparedStatement.setString(4, email);
        preparedStatement.setInt(5, id);
        preparedStatement.executeUpdate();
    }

    public void addToGroup(int id) {
        groupId = id;
    }

    public void removeFromGroup() {
        groupId = 0;
    }

    public void addTeamId(int id) {
        teamIds.add(id);
    }

    public void removeTeamId(int id) {
        teamIds.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return pseudonym;
    }
}
