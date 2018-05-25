package model;

import application.MainApplication;
import application.Session;
import sun.applet.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Group extends Party {

    public Group() {
        super();
    }

    public Group(int id) throws SQLException {
        super(id);
    }

    @Override
    public List<Integer> getPlayerIds() throws SQLException {
        sql = "SELECT * FROM group_player_mapping WHERE groupid = ?;";
        return super.getPlayerIds();
    }

    @Override
    protected void getData() throws SQLException {
        sql = "SELECT * FROM `group` WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
        }
    }

    @Override
    public List<Integer> getLeaderIds() throws SQLException {
        sql = "SELECT * FROM group_player_mapping WHERE groupid = ? AND leader = true;";
        return super.getLeaderIds();
    }

    @Override
    public void addLeader(Player player) throws SQLException {
        sql = "UPDATE group_player_mapping SET leader = true WHERE groupid = ? AND playerid = ?;";
        super.addLeader(player);
    }

    @Override
    public void removeLeader(Player player) throws SQLException {
        sql = "UPDATE group_player_mapping SET leader = false WHERE groupid = ? AND playerid = ?;";
        super.removeLeader(player);
    }

    @Override
    protected void create(String name) throws SQLException {
        // get the lowest id that is currently not used
        sql = "SELECT MIN(g1.id + 1) AS nextlowestid FROM `group` g1 " +
                "LEFT JOIN `group` g2 ON g1.id + 1 = g2.id " +
                "WHERE g2.id IS NULL;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.first()) {
            int lowestIdAvailable = resultSet.getInt("nextlowestid");
            lowestIdAvailable = lowestIdAvailable == 0 ? 1 : lowestIdAvailable;

            sql = "INSERT INTO `group`(id, name) VALUE(?, ?);";
            statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
            statement.setInt(1, lowestIdAvailable);
            statement.setString(2, name);
            statement.executeUpdate();
        }

        sql = "SELECT * FROM `group` WHERE name = ?;";
        super.create(name);
    }

    public void create(Player player) throws SQLException {
        create(player.getPseudonym());
        addPlayer(player, true);
    }

    @Override
    public void addPlayer(Player player, boolean isLeader) throws SQLException {
        sql = "INSERT INTO group_player_mapping (groupid, playerid, leader) VALUE(?, ?, ?);";
        super.addPlayer(player, isLeader);
    }

    @Override
    public void removePlayer(Player player) throws SQLException {
        sql = "DELETE FROM group_player_mapping WHERE groupid = ? AND playerid = ?;";
        super.removePlayer(player);
    }

    @Override
    public void delete() throws SQLException {
        sql = "DELETE FROM `group` WHERE id = ?;";
        super.delete();
    }

    @Override
    public void rename(String name) throws SQLException {
        sql = "UPDATE `group` SET name = ? WHERE id = ?;";
        super.rename(name);
    }

    public void exitOnLogout() throws SQLException {
        removePlayer(Session.getInstance().getPlayer());
    }

}
