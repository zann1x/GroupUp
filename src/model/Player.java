package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import application.MainApplication;

public class Player {

    private int id;
    private String forename;
    private String surname;
    private String pseudonym;
    private String region;
    private String email;

    public Player() {
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
    
    public String getRegion(){
    	return region;
    }

    public String getEmail() {
        return email;
    }

    public int getGroupId() throws SQLException {
        String sql = "SELECT * FROM group_player_mapping WHERE playerid = ? AND pendingjoin = false;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first())
            return resultSet.getInt("groupid");
        else
            return 0;
    }

    public List<Integer> getTeamIds() throws SQLException {
        String sql = "SELECT * FROM team_player_mapping WHERE playerid = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> teamIds = new ArrayList<>();
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
            
            Locale locale = new Locale("", rs.getString("region"));
            this.region = locale.getDisplayCountry();
            
            this.email = rs.getString("email");
        }
        getGroupId();
        getTeamIds();
    }
    
    public void refresh() throws SQLException {
    	getData();
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

    public String getSessionId() throws SQLException {
        String sql = "SELECT sessionid FROM player WHERE id = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.first())
            return resultSet.getString("sessionid");
        else
            return null;
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
    
    public void updatePlayer(String forename, String surname, String pseudonym, String password, String region, String email) throws SQLException{
    	String sql = "UPDATE player SET forename = ?, surname = ?, pseudonym = ?, password = ?, region = ?, email = ? WHERE id = ?";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, forename);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, pseudonym);
        preparedStatement.setString(4, password);
        
		String[] isos = Locale.getISOCountries();
		String iso = "";
		for (int i = 0; i < isos.length; i++) {
			Locale locale = new Locale("", isos[i]);
			if(locale.getDisplayCountry().equals(region)){
				iso = isos[i];
			}
		}
        preparedStatement.setString(5, iso);
        
        preparedStatement.setString(6, email);
        preparedStatement.setInt(7, id);
        preparedStatement.executeUpdate();
    }
    
    public void updatePlayer(String forename, String surname, String pseudonym, String region, String email) throws SQLException{
    	String sql = "UPDATE player SET forename = ?, surname = ?, pseudonym = ?, region = ?, email = ? WHERE id = ?";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, forename);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, pseudonym);
        
        String[] isos = Locale.getISOCountries();
		String iso = "";
		for (int i = 0; i < isos.length; i++) {
			Locale locale = new Locale("", isos[i]);
			if(locale.getDisplayCountry().equals(region)){
				iso = isos[i];
			}
		}
        preparedStatement.setString(4, iso);
        
        preparedStatement.setString(5, email);
        preparedStatement.setInt(6, id);
        preparedStatement.executeUpdate();
    }
    
    public List<String> getAllUnfriendPseudonyms() throws SQLException{
    	List<String> pseudonyms = new ArrayList<>();
        String sql = "SELECT pseudonym FROM player WHERE player.id NOT IN " +
        				"(SELECT friendid FROM friends WHERE playerid = ?) " +
        				"AND player.id NOT IN " +
        				"(SELECT playerid FROM friends WHERE friendid = ? AND pending = 1) " +
        				"AND player.id != ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, id);
        preparedStatement.setInt(3, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String pseudonym = resultSet.getString("pseudonym");
            pseudonyms.add(pseudonym);
        }
        return pseudonyms;
    }
    
    public Player getPlayerByPseudonym(String pseudonym) throws SQLException {
        String sql = "SELECT id FROM player WHERE pseudonym = ?;";
        PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        preparedStatement.setString(1, pseudonym);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        int id = resultSet.getInt("id");
        
        Player player = new Player(id);
        return player;
    }
    
    public void sendFriendRequest(Player newFriend) throws SQLException {
    	String sql = "INSERT INTO friends (playerid, friendid, pending) VALUES (?, ?, 1)";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
    	preparedStatement.setInt(1, id);
    	preparedStatement.setInt(2, newFriend.getId());
    	preparedStatement.execute();
    }
    
    public void acceptFriendRequest(Player newFriend) throws SQLException {
    	String sql1 = "UPDATE friends SET pending = 0 WHERE playerid = ? and friendid = ?";
    	PreparedStatement preparedStatement1 = MainApplication.instance.getDbConnector().prepareStatement(sql1);
    	preparedStatement1.setInt(1, newFriend.getId());
    	preparedStatement1.setInt(2, id);
    	
    	String sql2 = "INSERT INTO friends (playerid, friendid, pending) VALUES (?, ?, 0)";
    	PreparedStatement preparedStatement2 = MainApplication.instance.getDbConnector().prepareStatement(sql2);
    	preparedStatement2.setInt(1, id);
    	preparedStatement2.setInt(2, newFriend.getId());
    	
    	preparedStatement1.executeUpdate();
    	preparedStatement2.execute();
    }
    
    public List<Player> getPendingFriendRequest() throws SQLException {
    	String sql = "SELECT playerid FROM friends WHERE friendid = ? and pending = 1;";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
    	preparedStatement.setInt(1, id);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	
    	List<Player> PendingRequests = new ArrayList<>();
    	while(resultSet.next()){
    		int friendid = resultSet.getInt("playerid");
    		PendingRequests.add(new Player(friendid));
    	}
    	return PendingRequests;
    }
    
    public List<Player> getFriends() throws SQLException {
    	String sql = "SELECT friendid FROM friends WHERE playerid = ? and pending = 0;";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
    	preparedStatement.setInt(1, id);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	
    	List<Player> friends = new ArrayList<>();
    	while(resultSet.next()){
    		int friendid = resultSet.getInt("friendid");
    		friends.add(new Player(friendid));
    	}
    	return friends;
    }
    
    public void unFriend(Player friend) throws SQLException {
    	String sql = "DELETE FROM friends WHERE (playerid =  ? AND friendid = ?) OR (playerid = ? AND friendid = ?);";
    	PreparedStatement preparedStatement = MainApplication.instance.getDbConnector().prepareStatement(sql);
    	preparedStatement.setInt(1, id);
    	preparedStatement.setInt(2, friend.getId());
    	preparedStatement.setInt(3, friend.getId());
    	preparedStatement.setInt(4, id);
    	preparedStatement.execute();
    }

    @Override
    public String toString() {
        return pseudonym;
    }
    
    @Override
    public boolean equals(Object o){
    	if(o instanceof Player){
    		if(this.id == ((Player) o).getId()){
    			return true;
    		} else {
    			return false;
    		}
    	} else {
    		return false;
    	}
    }
}
